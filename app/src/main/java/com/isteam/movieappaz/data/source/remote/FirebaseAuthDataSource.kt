package com.isteam.movieappaz.data.source.remote

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.common.utils.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun registerUserWithEmailAndPassword(email: String, password: String): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                trySend(Resource.Success(it.user?.uid))
            }.addOnFailureListener {
                trySend(Resource.Error(it))
            }
            awaitClose { trySend(Resource.Success(null)) }
        }

    fun loginUserWithEmailAndPassword(email: String, password: String): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    trySend(Resource.Success(it.user?.uid))
                }.addOnFailureListener {
                    trySend(Resource.Error(it))
                }
            awaitClose { trySend(Resource.Success(null)) }
        }


    fun resetPassword(email: String): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading)
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            trySend(Resource.Success(true))
        }.addOnFailureListener {
            trySend(Resource.Error(it))
        }.await()
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }

    fun deleteCurrentUser(): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading)

        auth.currentUser?.let {
            it.delete().addOnSuccessListener {
                trySend(Resource.Success(true))
            }.addOnFailureListener {
                trySend(Resource.Error(it))
            }
        } ?: trySend(Resource.Success(false))

        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }

    fun logOut(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            auth.signOut()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    fun signInWithGoogle(account:GoogleSignInAccount):Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnSuccessListener {
                trySend(Resource.Success(it.user?.uid))
            }.addOnFailureListener {
                trySend(Resource.Error(it))
            }
            awaitClose{ trySend(Resource.Success(null))}
        }

    fun checkVerification(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            val isEmailVerification = auth.currentUser?.isEmailVerified ?: false
            emit(Resource.Success(isEmailVerification))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    fun sendVerificationEmail(): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            auth.currentUser?.let {
                it.sendEmailVerification()
                    .addOnSuccessListener {
                        trySend(Resource.Success(true))
                    }.addOnFailureListener {
                        trySend(Resource.Error(it))
                    }
            } ?: Resource.Error(Exception("User not found"))
        } catch (e: Exception) {
            trySend(Resource.Error(e))
        }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }


    fun deleteAccount(token: String): Flow<Resource<Boolean>> = callbackFlow {
        trySend(Resource.Loading)
        auth.currentUser?.let {
            it.delete().addOnSuccessListener {
                firestore.collection(Constants.USER_DB).document(token).delete()
                    .addOnSuccessListener {
                        firestore.collection(Constants.FAVORITES_DB).document(token).delete()
                            .addOnSuccessListener {
                                trySend(Resource.Success(true))
                            }
                    }
            }.addOnFailureListener {
                trySend(Resource.Error(it))
            }
        }
        awaitClose { trySend(Resource.Error(Exception("Error"))) }
    }


}