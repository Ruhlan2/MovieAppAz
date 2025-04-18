package com.isteam.movieappaz.presentation.ui.auth

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.data.service.google.GoogleClient
import com.isteam.movieappaz.domain.useCase.FirebaseAuthUseCase
import com.isteam.movieappaz.domain.useCase.FirebaseUseCase
import com.isteam.movieappaz.domain.useCase.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: FirebaseAuthUseCase,
    private val firebaseUseCase: FirebaseUseCase,
    private val validationUseCase: ValidationUseCase,
    private val spManager: SharedPreferencesManager,
    private val client: GoogleClient
) : BaseViewModel<AuthUiState>() {


    fun submitLogin(
        email: String,
        password: String,
        rememberMe: Boolean,
    ) {
        val executeEmail = validationUseCase.executeEmail(email)
        val executePassword = validationUseCase.executePassword(password)
        val executeList = listOf(executeEmail, executePassword)
        val hasError = executeList.any { !it.successful }

        if (hasError) {
            setState(
                AuthUiState.ValidationError(
                    executeList.first { !it.successful }.errorMessage ?: R.string.error
                )
            )
            return
        }

        login(email, password, rememberMe)

    }

    fun submitRegister(
        email: String,
        password: String,
        rememberMe: Boolean,
    ) {
        val executeEmail = validationUseCase.executeEmail(email)
        val executePassword = validationUseCase.executePassword(password)
        val executeList = listOf(executeEmail, executePassword)
        val hasError = executeList.any { !it.successful }

        if (hasError) {
            setState(
                AuthUiState.ValidationError(
                    executeList.first { !it.successful }.errorMessage ?: R.string.error
                )
            )
            return
        }
        register(email, password)
    }

    private fun register(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.registerUserWithEmailAndPassword(email, password).handleResult(
                onComplete = {
                    spManager.setIsRegisterFinished(true)
                    setState(AuthUiState.RegisterSuccess(it, true))
                },
                onLoading = {
                    setState(AuthUiState.Loading)
                },
                onError = {
                    setState(AuthUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    private fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            authUseCase.loginUserWithEmailAndPassword(email, password).handleResult(
                onComplete = {
                    setState(AuthUiState.LoginSuccess(it))
                },
                onLoading = {
                    setState(AuthUiState.Loading)
                },
                onError = {
                    setState(AuthUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun isEmailVerified(token: String) {
        viewModelScope.launch {
            authUseCase.checkVerification().handleResult(
                onComplete = {
                    setState(AuthUiState.IsVerified(it, token))
                },
                onLoading = {
                    setState(AuthUiState.Loading)
                },
                onError = {
                    setState(AuthUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun sendVerification() {
        viewModelScope.launch {
            authUseCase.sendVerification().handleResult(
                onComplete = {
                    setState(AuthUiState.VerificationSent)
                },
                onLoading = {
                    setState(AuthUiState.Loading)
                },
                onError = {
                    setState(AuthUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }



    fun launch(launcher: ActivityResultLauncher<Intent>){
        val signInIntent=client.signInWithGoogle().signInIntent
        launcher.launch(signInIntent)
    }


    fun handleSignInResults(task:Task<GoogleSignInAccount>){
        if (task.isSuccessful) {
            loginWithGoogle(task.result)
        } else {
            setState(AuthUiState.Error(task.exception.toString()))
        }
    }

    fun deleteUser() {
        authUseCase.deleteUser().handleResult(
            onComplete = {
                spManager.setIsRegisterFinished(true)
                setState(AuthUiState.DeleteSuccess)
            },
            onLoading = {
                setState(AuthUiState.Loading)
            },
            onError = {
                setState(AuthUiState.Error(it.localizedMessage as String))
            }
        )
    }

    fun signOutUser(){
        authUseCase.signOut().handleResult(
            onComplete = {
                spManager.removeToken()
                spManager.setRemember(false)
                setState(AuthUiState.SignOutSuccess)
            },
            onLoading = {
                setState(AuthUiState.Loading)
            },
            onError = {
                setState(AuthUiState.Error(it.localizedMessage as String))
            }
        )
    }

    fun submitForgotPass(email: String) {
        val executeEmail = validationUseCase.executeEmail(email)

        if (!executeEmail.successful) {
            setState(AuthUiState.ValidationError(executeEmail.errorMessage ?: R.string.error))
            return
        }

        forgotPassword(email)

    }

    fun changeRegisterFinishedStatus(isFinished: Boolean) {
        viewModelScope.launch {
            spManager.setIsRegisterFinished(isFinished)
            setState(AuthUiState.RegisterSuccess(isFinished = isFinished))
        }
    }

    fun createUser(
        uuid: String,
        email: String,
        nickname: String,
        fullName: String,
        image: Uri?,
        rememberMe: Boolean,
        isGoogleSignIn: Boolean = false
    ) {
        val executeNickname = validationUseCase.executeNickname(nickname)
        val executeFullName = validationUseCase.executeNickname(fullName)
        val executeList = listOf(executeFullName, executeNickname)

        val hasError = executeList.any { !it.successful }

        if (hasError) {
            setState(
                AuthUiState.ValidationError(
                    executeList.first { !it.successful }.errorMessage ?: R.string.error
                )
            )
            return
        }

        val data = HashMap<String, Any>()
        data[Constants.EMAIL] = email
        data[Constants.FULL_NAME] = fullName
        data[Constants.NICKNAME] = nickname

        firebaseUseCase.createUserDatabase(
            uuid, image, data
        ).handleResult(
            onComplete = {
                if (isGoogleSignIn) {
                    setState(AuthUiState.GoogleSignInSuccess(uuid))
                } else {
                    setState(AuthUiState.CreateUserSuccess(rememberMe))
                }
            },
            onError = {
                setState(AuthUiState.Error(it.localizedMessage as String))
            },
            onLoading = {
                setState(AuthUiState.Loading)
            }
        )

    }

    fun setToken(token: String, rememberMe: Boolean) {
        spManager.setToken(token, rememberMe)

    }

    fun getIsRegisterFinished() {
        setState(AuthUiState.IsRegisterFinished(spManager.getIsRegisterFinished()))
    }

    private fun forgotPassword(email: String) {
        authUseCase.resetPassword(email).handleResult(
            onComplete = {
                setState(AuthUiState.ForgotPassSuccess)
            },
            onError = {
                setState(AuthUiState.Error(it.localizedMessage as String))
            },
            onLoading = {
                setState(AuthUiState.Loading)
            }
        )
    }

    private fun loginWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            authUseCase.signInWithGoogle(account).handleResult(
                onComplete = {
                    checkIsUserHave(it, account)
                },
                onLoading = {
                    setState(AuthUiState.Loading)
                },
                onError = {
                    setState(AuthUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    private fun checkIsUserHave(token: String, account: GoogleSignInAccount) {
        viewModelScope.launch {
            firebaseUseCase.isUserHave(token).handleResult(
                onComplete = {
                    if (it) {
                        setState(AuthUiState.GoogleSignInSuccess(token))
                    } else {
                        createUser(
                            token,
                            account.email.orEmpty(),
                            account.displayName.orEmpty(),
                            account.displayName.orEmpty(),
                            account.photoUrl,
                            rememberMe = true,
                            isGoogleSignIn = true
                        )
                    }
                },
                onError = {
                    setState(AuthUiState.Error(it.localizedMessage as String))
                },
                onLoading = {
                    setState(AuthUiState.Loading)
                }
            )
        }
    }

}

sealed class AuthUiState : State {
    data class Error(val message: String) : AuthUiState()

    data class ValidationError(val message: Int) : AuthUiState()

    data object Loading : AuthUiState()

    data object DeleteSuccess : AuthUiState()

    data object SignOutSuccess : AuthUiState()

    data class IsRegisterFinished(val isFinished: Boolean) : AuthUiState()

    data class RegisterSuccess(val token: String = "", val isFinished: Boolean = false) :
        AuthUiState()

    data class LoginSuccess(val token: String) : AuthUiState()

    data object ForgotPassSuccess : AuthUiState()

    data class CreateUserSuccess(val rememberMe: Boolean) : AuthUiState()

    data class GoogleSignInSuccess(val token:String):AuthUiState()

    data class IsVerified(val isVerified: Boolean, val token: String = "") : AuthUiState()

    data object VerificationSent : AuthUiState()

}