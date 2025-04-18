package com.isteam.movieappaz.presentation.ui.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.common.utils.ThemeEnum
import com.isteam.movieappaz.common.utils.ThemeParameters
import com.isteam.movieappaz.data.dto.remote.profile.EditProfileDTO
import com.isteam.movieappaz.domain.model.profile.ProfileUiModel
import com.isteam.movieappaz.domain.useCase.FirebaseAuthUseCase
import com.isteam.movieappaz.domain.useCase.FirebaseUseCase
import com.isteam.movieappaz.domain.useCase.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sp: SharedPreferencesManager,
    private val firebaseUseCase: FirebaseUseCase,
    private val validationUseCase: ValidationUseCase,
    private val authUseCase: FirebaseAuthUseCase
) : BaseViewModel<ProfileUiState>() {

    val token = sp.getToken()

    fun getUserDetails() {
        viewModelScope.launch {

            firebaseUseCase.getUserDetails(token).handleResult(
                onComplete = {
                    setState(ProfileUiState.ProfileDetails(it))
                },
                onLoading = {
                    setState(ProfileUiState.Loading)
                },
                onError = {
                    setState(ProfileUiState.Error(it.localizedMessage as String))
                }
            )
        }

    }

    fun getThemeParameters() = ThemeParameters(sp.getIsThemeSelectedFromApp(), sp.getDarkMode())


    fun submitEdit(fullName: String, nickName: String, image: Uri?) {
            val name=validationUseCase.executeName(fullName)
            val nick=validationUseCase.executeNickname(nickName)
            val executeList= listOf(name,nick)

            val hasError=executeList.any { !it.successful }
            if (hasError){
                setState(
                    ProfileUiState.EditProfileError(
                        (executeList.first { !it.successful }.errorMessage ?: R.string.error)
                    )
                )
                return
            }

        editProfile(fullName, nickName, image)


    }

    private fun editProfile(fullName: String, nickName: String, image: Uri?) {
        viewModelScope.launch {
            firebaseUseCase.editProfile(sp.getToken(), EditProfileDTO(fullName, nickName, image))
                .handleResult(
                onComplete = {
                    setState(ProfileUiState.EditProfileSuccess)
                },
                onLoading = {
                    setState(ProfileUiState.Loading)
                },
                onError = {
                    setState(ProfileUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authUseCase.deleteAccount(token)
                .handleResult(
                    onComplete = {
                        sp.removeToken()
                        sp.setRemember(false)
                        setState(ProfileUiState.DeleteSuccess)
                    },
                    onLoading = {
                        setState(ProfileUiState.Loading)
                    },
                    onError = {
                        setState(ProfileUiState.Error(it.localizedMessage as String))
                    }
                )
        }
    }

    fun getLanguageCode() {
        val languageCode = sp.getLanguageCode()
        setState(ProfileUiState.Language(languageCode))
    }

    fun setLanguageCode(languageCode: LanguageCode) = sp.setLanguageCode(languageCode)

    fun setIsThemeSelectedFromApp(isSelected:Boolean) =sp.setIsThemeSelectedFromApp(isSelected)

    fun setTheme(theme: ThemeEnum) = sp.setDarkMode(theme)

}

sealed class ProfileUiState : State {

    data object Loading : ProfileUiState()

    data class Error(val message: String) : ProfileUiState()

    data class EditProfileError(val message: Int) : ProfileUiState()

    data class ProfileDetails(val data: ProfileUiModel) : ProfileUiState()

    data class Language(val languageCode: LanguageCode) : ProfileUiState()

    data object EditProfileSuccess : ProfileUiState()

    data object DeleteSuccess : ProfileUiState()

}