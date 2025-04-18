package com.isteam.movieappaz.presentation.ui.movieList

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.domain.model.MovieUiModel
import com.isteam.movieappaz.domain.useCase.MovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val movieListUseCase: MovieListUseCase) :
    BaseViewModel<MovieListUiState>() {

    fun getMovieListData(movieTypeEnum: MovieTypeEnum) {
        viewModelScope.launch {
            movieListUseCase.getPopularPagingCase(movieTypeEnum).cachedIn(viewModelScope)
                .collectLatest {
                    setState(MovieListUiState.PagingMovieList(it))
                }
        }
    }
}

sealed class MovieListUiState : State {

    data class PagingMovieList(val list: PagingData<MovieUiModel>) : MovieListUiState()

}