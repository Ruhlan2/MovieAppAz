package com.isteam.movieappaz.data.source.remote

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.data.dto.remote.favorites.FavoritesBody
import com.isteam.movieappaz.data.dto.remote.favorites.FavoritesDTO
import com.isteam.movieappaz.data.dto.remote.notifications.NotificationsDTO
import com.isteam.movieappaz.data.dto.remote.profile.EditProfileDTO
import com.isteam.movieappaz.data.dto.remote.profile.ProfileDTO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val imageStorage = storage.reference

    fun createUserDatabase(
        uuid: String,
        image: Uri?,
        data: HashMap<String, Any>,
    ): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading)
        var imagePath = "null"
        data[Constants.IMAGE_URL] = imagePath
        try {
            if (image != null) {
                val imageSource = "profilePicture/$uuid.jpg"
                imageStorage.child(imageSource).putFile(image).addOnSuccessListener {
                    storage.getReference(imageSource).downloadUrl.addOnSuccessListener { uriLink ->
                        imagePath = uriLink.toString()
                        data[Constants.IMAGE_URL] = imagePath

                        fireStore.collection(Constants.USER_DB).document(uuid).set(data)
                            .addOnSuccessListener {
                                trySend(Resource.Success(true))
                            }.addOnFailureListener {
                                trySend(Resource.Error(it))
                            }
                    }.addOnFailureListener {
                        trySend(Resource.Error(it))
                    }
                }.addOnFailureListener {
                    trySend(Resource.Error(it))
                }.await()
            } else {
                fireStore.collection(Constants.USER_DB).document(uuid).set(data)
                    .addOnSuccessListener {
                        trySend(Resource.Success(true))
                    }.addOnFailureListener {
                    trySend(Resource.Error(it))
                }.await()
            }
        } catch (e: Exception) {
            trySend(Resource.Error(e))
        }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }

    fun isUserHave(token: String): Flow<Resource<Boolean>> = callbackFlow {
        try {
            fireStore.collection(Constants.USER_DB)
                .document(token)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(Resource.Success(it.result.exists()))
                    } else {
                        trySend(Resource.Error(it.exception ?: Exception("Error")))
                    }
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e))
        }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }

    fun getUserDetails(token: String): Flow<Resource<ProfileDTO>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            fireStore.collection(Constants.USER_DB).document(token).get().addOnSuccessListener {
                it.data?.let { data ->
                    val email = data[Constants.EMAIL] as String?
                    val fullName = data[Constants.FULL_NAME] as String?
                    val imageUrl = data[Constants.IMAGE_URL] as String?
                    val nickname = data[Constants.NICKNAME] as String?

                    trySend(
                        Resource.Success(
                            ProfileDTO(email, fullName, imageUrl, nickname)
                        )
                    )
                }
            }.addOnFailureListener {
                trySend(Resource.Error(it))
            }.await()
        } catch (e: Exception) {
            trySend(Resource.Error(e))
        }

        awaitClose { trySend(Resource.Error(Exception("Error"))) }

    }

    fun setFavorites(token: String, favorites: FavoritesBody): Flow<Resource<Boolean>> =
        callbackFlow {
            trySend(Resource.Loading)
            try {
                val data = hashMapOf(
                    Constants.ID to favorites.movieId,
                    Constants.TITLE to favorites.title,
                    Constants.DESCRIPTION to favorites.description,
                    Constants.IMAGE_URL to favorites.imageUrl
                )

                fireStore.collection(Constants.FAVORITES_DB).document(token)
                    .collection(Constants.FAVORITES).add(data)
                    .addOnSuccessListener {
                        trySend(Resource.Success(true))
                    }.addOnFailureListener {
                        Resource.Error(it)
                    }
            } catch (e: Exception) {
                trySend(Resource.Error(e))
            }
            awaitClose { trySend(Resource.Error(Exception("Error"))) }
        }

    fun getLiveFavorites(token: String): Flow<Resource<List<FavoritesDTO>>> = callbackFlow {
        trySend(Resource.Loading)
        val dataList = arrayListOf<FavoritesDTO>()

        try {
            fireStore.collection(Constants.FAVORITES_DB).document(token)
                .collection(Constants.FAVORITES)
                .addSnapshotListener { value, error ->
                    dataList.clear()
                    error?.let {
                        trySend(Resource.Error(it))
                    }
                    value?.let {
                        it.forEach { data ->
                            val uid = data.id
                            val imageUrl = data.data[Constants.IMAGE_URL] as String?
                            val movieId = data.data[Constants.ID] as Long?
                            val title = data.data[Constants.TITLE] as String?
                            val description = data.data[Constants.DESCRIPTION] as String?
                            dataList.add(
                                FavoritesDTO(
                                    uid,
                                    movieId,
                                    title,
                                    description,
                                    imageUrl
                                )
                            )
                        }
                        trySend(Resource.Success(dataList))
                    } ?: trySend(Resource.Success(emptyList()))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e))
        }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }

    fun getIsInFavorites(token: String, id: Int): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            fireStore.collection(Constants.FAVORITES_DB)
                .document(token)
                .collection(Constants.FAVORITES)
                .whereEqualTo(Constants.ID, id)
                .get()
                .addOnSuccessListener {
                    trySend(Resource.Success(!it.isEmpty))
                }.addOnFailureListener {
                    trySend(Resource.Success(false))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e))
        }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }

    fun removeFavorite(token: String, id: Long): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            fireStore.collection(Constants.FAVORITES_DB)
                .document(token)
                .collection(Constants.FAVORITES)
                .whereEqualTo(Constants.ID, id)
                .get().addOnSuccessListener {
                    it.documents.getOrNull(0)?.let {
                        fireStore.collection(Constants.FAVORITES_DB)
                            .document(token)
                            .collection(Constants.FAVORITES)
                            .document(it.id)
                            .delete()
                            .addOnSuccessListener {
                                trySend(Resource.Success(true))
                            }.addOnFailureListener {
                                trySend(Resource.Error(it))
                            }
                    } ?: trySend(Resource.Error(Exception("Error")))
                }.addOnFailureListener {
                    trySend(Resource.Error(it))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e))
        }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }

    fun editProfile(token: String, editProfileDTO: EditProfileDTO): Flow<Resource<Boolean>> =
        callbackFlow {
            trySend(Resource.Loading)
            try {
                if (editProfileDTO.image != null) {
                    val uuid = UUID.randomUUID()
                    val imageSource = "profilePicture/$uuid.jpg"
                    imageStorage
                        .child(imageSource)
                        .putFile(editProfileDTO.image)
                        .addOnSuccessListener {
                            storage
                                .getReference(imageSource)
                                .downloadUrl
                                .addOnSuccessListener {
                                    val data = mapOf(
                                        Constants.FULL_NAME to editProfileDTO.fullName,
                                        Constants.NICKNAME to editProfileDTO.nickName,
                                        Constants.IMAGE_URL to it.toString()
                                    )

                                    fireStore
                                        .collection(Constants.USER_DB)
                                        .document(token)
                                        .update(data).addOnSuccessListener {
                                            trySend(Resource.Success(true))
                                        }.addOnFailureListener {
                                            trySend(Resource.Error(it))
                                        }
                                }.addOnFailureListener {
                                    trySend(Resource.Error(it))
                                }
                        }.addOnFailureListener {
                            trySend(Resource.Error(it))
                        }
                } else {
                    val data = mapOf(
                        Constants.FULL_NAME to editProfileDTO.fullName,
                        Constants.NICKNAME to editProfileDTO.nickName,
                    )
                    fireStore
                        .collection(Constants.USER_DB)
                        .document(token)
                        .update(data).addOnSuccessListener {
                            trySend(Resource.Success(true))
                        }.addOnFailureListener {
                            trySend(Resource.Error(it))
                        }
                }
            } catch (e: Exception) {
                trySend(Resource.Error(e))
            }

            awaitClose { trySend(Resource.Error(Exception("Error"))) }
        }

    fun getNotifications(): Flow<Resource<List<NotificationsDTO>>> = callbackFlow {
        trySend(Resource.Loading)
        val notificationList = arrayListOf<NotificationsDTO>()
        fireStore.collection(Constants.NOTIFICATIONS)
            .get()
            .addOnSuccessListener {
                it.documents.forEachIndexed { index, documentSnapshot ->
                    notificationList.add(
                        NotificationsDTO(
                            index,
                            documentSnapshot[Constants.TITLE] as String?,
                            documentSnapshot[Constants.DESCRIPTION] as String?,
                            documentSnapshot[Constants.IMAGE_URL] as String?,
                        )
                    )
                }
                trySend(Resource.Success(notificationList))
            }.addOnFailureListener {
                trySend(Resource.Error(it))
            }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }


}