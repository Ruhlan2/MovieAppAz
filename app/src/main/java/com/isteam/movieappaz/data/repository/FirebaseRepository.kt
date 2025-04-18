package com.isteam.movieappaz.data.repository

import android.net.Uri
import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.data.dto.remote.favorites.FavoritesBody
import com.isteam.movieappaz.data.dto.remote.profile.EditProfileDTO
import com.isteam.movieappaz.data.mapper.toListFavoritesUiModel
import com.isteam.movieappaz.data.mapper.toNotificationListUiModel
import com.isteam.movieappaz.data.mapper.toProfileUiModel
import com.isteam.movieappaz.data.source.remote.FirebaseDataSource
import com.isteam.movieappaz.domain.model.favorites.FavoritesUiModel
import com.isteam.movieappaz.domain.model.notifications.NotificationUiModel
import com.isteam.movieappaz.domain.model.profile.ProfileUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val source: FirebaseDataSource
) {

    fun createUserDatabase(
        uuid: String,
        image: Uri?,
        data: HashMap<String, Any>,
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.createUserDatabase(uuid, image, data).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun getUserDetails(token: String): Flow<Resource<ProfileUiModel>> = flow {
        emit(Resource.Loading)
        source.getUserDetails(token).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result.toProfileUiModel()))
            }
        }
    }

    fun isUserHave(token: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.isUserHave(token).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun setFavorites(token: String, favorites: FavoritesBody): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.setFavorites(token, favorites).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun getLiveFavorites(token: String): Flow<Resource<List<FavoritesUiModel>>> = flow {
        emit(Resource.Loading)
        source.getLiveFavorites(token).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result.toListFavoritesUiModel()))
            }
        }
    }

    fun getIsInFavorites(token: String, id: Int): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.getIsInFavorites(token, id).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun removeFavorite(token: String, id: Long): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.removeFavorite(token, id).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun editProfile(token: String, editProfileDTO: EditProfileDTO): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            source.editProfile(token, editProfileDTO).collect {
                when (it) {
                    is Resource.Error -> emit(Resource.Error(it.throwable))
                    Resource.Loading -> Unit
                    is Resource.Success -> emit(Resource.Success(it.result))
                }
            }
        }

    fun getNotificationsList(): Flow<Resource<List<NotificationUiModel>>> = flow {
        emit(Resource.Loading)
        source.getNotifications().collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result.toNotificationListUiModel()))
            }
        }
    }

}