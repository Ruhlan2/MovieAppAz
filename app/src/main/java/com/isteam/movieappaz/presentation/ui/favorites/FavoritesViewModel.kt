package com.isteam.movieappaz.presentation.ui.favorites

import androidx.lifecycle.viewModelScope
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.domain.model.favorites.FavoritesUiModel
import com.isteam.movieappaz.domain.useCase.FirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val useCase: FirebaseUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<FavoritesUiState>() {

    val token = sharedPreferencesManager.getToken()

    init {
        getFavorites()
    }

    private fun getFavorites() {
        useCase.getLiveFavorites(token).handleResult(
            onComplete = {
                setState(FavoritesUiState.Favorites(it))
            },
            onLoading = {
                setState(FavoritesUiState.Loading)
            },
            onError = {
                setState(FavoritesUiState.Error(it.localizedMessage as String))
            }
        )
    }

    fun removeFavorite(id: Long) {
        viewModelScope.launch {
            useCase.removeFavorite(token, id).handleResult(
                onComplete = {
                    setState(FavoritesUiState.Deleted)
                },
                onError = {
                    setState(FavoritesUiState.Error(it.localizedMessage as String))
                },
                onLoading = {
                    setState(FavoritesUiState.Loading)
                }
            )
        }
    }

    fun onStopLoading() = setState(FavoritesUiState.OnStopLoading)

}

sealed class FavoritesUiState : State {

    data object Loading : FavoritesUiState()

    data object Deleted : FavoritesUiState()

    data class Error(val message: String) : FavoritesUiState()

    data class Favorites(val data: List<FavoritesUiModel>) : FavoritesUiState()

    data object OnStopLoading : FavoritesUiState()

}