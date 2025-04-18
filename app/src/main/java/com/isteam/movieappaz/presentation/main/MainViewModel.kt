package com.isteam.movieappaz.presentation.main

import androidx.lifecycle.viewModelScope
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.network.ConnectivityObserver
import com.isteam.movieappaz.common.network.NetworkConnectivityObserver
import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.common.utils.ThemeEnum
import com.isteam.movieappaz.common.utils.ThemeParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<MainUiState>() {

    private lateinit var themeEnum: ThemeEnum

    init {
        observeNetworks()
        getLanguage()
    }

    fun isInitializedTheme() = this::themeEnum.isInitialized

    fun setTheme(themeEnum: ThemeEnum) {
        this.themeEnum = themeEnum
    }

    fun getTheme() = themeEnum

    fun getThemeFromSP() = sharedPreferencesManager.getDarkMode()

    fun getThemeParameters() = ThemeParameters(
        sharedPreferencesManager.getIsThemeSelectedFromApp(),
        sharedPreferencesManager.getDarkMode()
    )

    fun getIsThemeSelectedFromApp() = sharedPreferencesManager.getIsThemeSelectedFromApp()

    private fun observeNetworks() {
        viewModelScope.launch {
            networkConnectivityObserver.connectionObserve().collectLatest {
                setState(MainUiState.NetworkStatus(it))
            }
        }
    }

    fun getLanguage() {
        setState(MainUiState.Language(sharedPreferencesManager.getLanguageCode()))
    }

    fun getLanguageCode() = sharedPreferencesManager.getLanguageCode()
}


sealed class MainUiState : State {
    data class Language(val lanCode: LanguageCode) : MainUiState()

    data class NetworkStatus(val status: ConnectivityObserver.ConnectionStatus) : MainUiState()
}