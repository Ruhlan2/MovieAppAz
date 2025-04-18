package com.isteam.movieappaz.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.domain.useCase.CheckConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val spManager: SharedPreferencesManager,
    private val connectionUseCase: CheckConnectionUseCase
) : BaseViewModel<SplashUiState>() {


    private var isConnectionEstablished=false

    fun getCredentials(){
        setState(
            SplashUiState.Initial(
                spManager.getIsOnboardingFinished(),
                spManager.getToken(),
                spManager.getRemember(),
                spManager.getLanguageCode()
            )
        )
    }
    fun getInitial() {
        checkInternetConnection()
    }

    private fun checkInternetConnection() {
        viewModelScope.launch {
            connectionUseCase.checkConnection().handleResult(
                onComplete = {
                    setState(SplashUiState.InternetConnection(it))
                    isConnectionEstablished=it
                 },
                onLoading = {

                },
                onError = {
                    setState(SplashUiState.InternetError(it.localizedMessage as String))
                }
            )
        }
    }
}

sealed class SplashUiState : State {

    data class Initial(
        val isFinished: Boolean,
        val token: String,
        val remember: Boolean,
        val languageCode: LanguageCode
    ) :
        SplashUiState()

    data class InternetConnection(val connection:Boolean):SplashUiState()

    data class InternetError(val error:String):SplashUiState()
}