package com.isteam.movieappaz.presentation.svm

import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsSharedViewModel @Inject constructor() :
    BaseViewModel<MovieDetailsSharedUiState>() {

    private var movieId: Int = -1

    fun setId(id: Int) {
        movieId = id
        setState(MovieDetailsSharedUiState.MovieId(movieId))
    }
}

sealed class MovieDetailsSharedUiState : State {

    data class MovieId(val id: Int) : MovieDetailsSharedUiState()

}