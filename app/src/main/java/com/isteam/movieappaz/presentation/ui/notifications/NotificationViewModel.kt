package com.isteam.movieappaz.presentation.ui.notifications

import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.domain.model.notifications.NotificationUiModel
import com.isteam.movieappaz.domain.useCase.FirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val useCase: FirebaseUseCase
) : BaseViewModel<NotificationsUiState>() {

    init {
        getNotifications()
    }

    private fun getNotifications() {
        useCase.getNotificationsList().handleResult(
            onComplete = {
                setState(NotificationsUiState.Success(it))
            },
            onError = {
                setState(NotificationsUiState.Error(it.localizedMessage as String))
            },
            onLoading = {
                setState(NotificationsUiState.Loading)
            }
        )
    }

}

sealed class NotificationsUiState : State {

    data object Loading : NotificationsUiState()

    data class Error(val message: String) : NotificationsUiState()

    data class Success(val notifications: List<NotificationUiModel>) : NotificationsUiState()

}