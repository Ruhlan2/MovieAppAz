package com.isteam.movieappaz.domain.useCase

import android.net.Uri
import com.isteam.movieappaz.data.dto.remote.favorites.FavoritesBody
import com.isteam.movieappaz.data.dto.remote.profile.EditProfileDTO
import com.isteam.movieappaz.data.repository.FirebaseRepository
import javax.inject.Inject

class FirebaseUseCase @Inject constructor(private val repo: FirebaseRepository) {

    fun createUserDatabase(
        uuid: String,
        image: Uri?,
        data: HashMap<String, Any>,
    ) = repo.createUserDatabase(uuid, image, data)

    fun getUserDetails(token: String) = repo.getUserDetails(token)

    fun isUserHave(token: String) = repo.isUserHave(token)

    fun getLiveFavorites(token: String) = repo.getLiveFavorites(token)

    fun setFavorites(token: String, favoritesUiModel: FavoritesBody) =
        repo.setFavorites(token, favoritesUiModel)

    fun getIsInFavorite(token: String, id: Int) = repo.getIsInFavorites(token, id)

    fun removeFavorite(token: String, id: Long) = repo.removeFavorite(token, id)

    fun editProfile(token: String,editProfileDTO: EditProfileDTO)=repo.editProfile(token,editProfileDTO)

    fun getNotificationsList() = repo.getNotificationsList()

}