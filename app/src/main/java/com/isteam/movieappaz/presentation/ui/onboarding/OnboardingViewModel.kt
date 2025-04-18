package com.isteam.movieappaz.presentation.ui.onboarding

import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.LanguageCode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val spManager: SharedPreferencesManager) :
    BaseViewModel<OnboardingUiState>() {

    fun setOnboardingFinished() {
        spManager.setIsOnboardingFinished(true)
        setState(OnboardingUiState.Finish)
    }

    fun getLanguageCode() {
        setState(OnboardingUiState.Language(spManager.getLanguageCode()))
    }

    fun setLanguageCode(languageCode: LanguageCode) {
        spManager.setLanguageCode(languageCode)
        setState(OnboardingUiState.Language(languageCode))
    }

    fun setSelectedLanguageState() = setState(OnboardingUiState.SelectedLanguage)

}

sealed class OnboardingUiState : State {

    data object Finish : OnboardingUiState()

    data class Language(val languageCode: LanguageCode) : OnboardingUiState()

    data object SelectedLanguage : OnboardingUiState()

}