package com.isteam.movieappaz.presentation.ui.share

import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<ShareUiState>() {

    fun getLanguageCode() = sharedPreferencesManager.getLanguageCode()

}

sealed class ShareUiState : State