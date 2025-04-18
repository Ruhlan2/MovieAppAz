package com.isteam.movieappaz.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.domain.model.MovieUiModel
import com.isteam.movieappaz.domain.model.profile.ProfileUiModel
import com.isteam.movieappaz.domain.useCase.FirebaseUseCase
import com.isteam.movieappaz.domain.useCase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val firebaseUseCase: FirebaseUseCase,
    private val spManager: SharedPreferencesManager
) : BaseViewModel<HomeUiState>() {

    private val trendingMovieList = arrayListOf<MovieUiModel>()
    val token=spManager.getToken()
    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            firebaseUseCase.getUserDetails(token = token).handleResult(
                onComplete = {
                    setState(HomeUiState.ProfileData(it))
                }, onError = {
                    setState(HomeUiState.Error(it.message.orEmpty()))
                }, onLoading = {
                    setState(HomeUiState.Loading)
                }
            )
        }
    }


    fun changeMovie() {
        trendingMovieList.shuffled().firstOrNull()?.let {
            setState(HomeUiState.ChangedMovie(it))
        }
    }

    fun getHomeData() {
        getPopularMovies()
        getNowPlayingMovies()
        getUpComingMovies()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            homeUseCase.getPopularMoviesCase().handleResult(
                onComplete = {
                    trendingMovieList.clear()
                    trendingMovieList.addAll(it)
                    val item = it.shuffled().first()
                    val list = it.shuffled().take(5)
                        .map { it1 -> SlideModel(Constants.BASE_URL_IMAGE + it1.backdropPath) }
                    setState(HomeUiState.PopularMovies(it, list, item))
                }, onError = {
                    setState(HomeUiState.Error(it.message.orEmpty()))
                }, onLoading = {

                })
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            homeUseCase.getNowPlayingMoviesCase().handleResult(onComplete = {
                setState(HomeUiState.NowPlayingMovies(it))
            }, onError = {
                setState(HomeUiState.Error(it.message.orEmpty()))
            }, onLoading = {

            })
        }
    }

    private fun getUpComingMovies() {
        viewModelScope.launch {
            homeUseCase.getUpComingMoviesCase().handleResult(onComplete = {
                setState(HomeUiState.UpComingMovies(it))
            }, onError = {
                setState(HomeUiState.Error(it.message.orEmpty()))
            }, onLoading = {

            })
        }
    }

    fun onStopLoading() = setState(HomeUiState.OnStopLoading)

}

sealed class HomeUiState : State {

    data class ProfileData(val data: ProfileUiModel) : HomeUiState()

    data class PopularMovies(
        val popularMovies: List<MovieUiModel> = emptyList(),
        val imageList: List<SlideModel> = emptyList(),
        val movie: MovieUiModel
    ) : HomeUiState()

    data class ChangedMovie(val item: MovieUiModel) : HomeUiState()

    data class NowPlayingMovies(val nowPlayingMovies: List<MovieUiModel> = emptyList()) :
        HomeUiState()

    data class UpComingMovies(val upComingMovies: List<MovieUiModel> = emptyList()) : HomeUiState()

    data class Error(val message: String) : HomeUiState()

    data object Loading : HomeUiState()

    data object OnStopLoading : HomeUiState()
}