package com.isteam.movieappaz.presentation.ui.details

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.data.dto.remote.favorites.FavoritesBody
import com.isteam.movieappaz.domain.model.MovieUiModel
import com.isteam.movieappaz.domain.model.credits.CreditsUiModel
import com.isteam.movieappaz.domain.model.details.images.BackdropUiModel
import com.isteam.movieappaz.domain.model.details.movieDetails.MovieDetailsUiModel
import com.isteam.movieappaz.domain.model.details.reviews.DetailsResultUiModel
import com.isteam.movieappaz.domain.model.details.reviews.ReviewDetailsUiModel
import com.isteam.movieappaz.domain.model.details.video.VideoResultUiModel
import com.isteam.movieappaz.domain.useCase.DetailsUseCase
import com.isteam.movieappaz.domain.useCase.FirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val useCase: DetailsUseCase,
    private val firebaseUseCase: FirebaseUseCase,
    private val spManager: SharedPreferencesManager
) : BaseViewModel<MovieUiState>() {

    private val token = spManager.getToken()
    private var isInFavorite = false

    fun initialCall(id: Int) {
        getDetails(id)
        getCredits(id)
        getImages(id)
        getIsInFavorite(id)
    }

    fun getLanguageCode() = spManager.getLanguageCode()

    fun loadVideo(id: Int) {
        viewModelScope.launch {
            useCase.getMovieVideos(id).handleResult(
                onComplete = {
                    val videoList = it.result.reversed()
                    setState(MovieUiState.Videos(videoList))
                },
                onLoading = {
                    setState(MovieUiState.Loading)
                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun getMovieReviewsFirstPage(id: Int) {
        viewModelScope.launch {
            useCase.getMovieReviewsFirstPage(id).handleResult(
                onComplete = {
                    setState(MovieUiState.InitialReviews(it))
                },
                onLoading = {
                    setState(MovieUiState.Loading)
                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun getMovieRecommendations(id: Int) {
        viewModelScope.launch {
            useCase.getMovieRecommendation(MovieTypeEnum.RECOMMENDATIONS, id)
                .cachedIn(viewModelScope).collectLatest {
                setState(MovieUiState.Recommendations(it))
            }
        }
    }

    private fun getDetails(id: Int) {
        viewModelScope.launch {
            useCase.getMovieDetails(id).handleResult(
                onComplete = {
                    setState(MovieUiState.MovieDetails(it))
                },
                onLoading = {
                    setState(MovieUiState.Loading)
                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    private fun getCredits(id: Int) {
        viewModelScope.launch {
            useCase.getMovieCredits(id).handleResult(
                onComplete = {
                    setState(MovieUiState.Credits(it))
                },
                onLoading = {

                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    private fun getImages(id: Int) {
        viewModelScope.launch {
            useCase.getMovieImages(id).handleResult(
                onComplete = {
                    val newList = it.backdrops.shuffled().take(5)
                    setState(MovieUiState.Images(newList))
                },
                onLoading = {

                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun getAllImages(id: Int) {
        viewModelScope.launch {
            useCase.getMovieImages(id).handleResult(
                onComplete = {
                    setState(MovieUiState.Images(it.backdrops))
                },
                onLoading = {

                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun getReviews(id: Int) {
        viewModelScope.launch {
            useCase.getMovieReviews(id).collectLatest {
                setState(MovieUiState.Reviews(it))
            }
        }
    }

    private fun getIsInFavorite(id: Int) {
        viewModelScope.launch {
            firebaseUseCase.getIsInFavorite(token, id).handleResult(
                onComplete = {
                    isInFavorite = it
                    setState(MovieUiState.IsInFavorite(it, true))
                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                },
                onLoading = {
                    setState(MovieUiState.Loading)
                }
            )
        }
    }

    fun changeFavorite(favoritesBody: FavoritesBody) {
        if (isInFavorite) {
            removeFavorite(favoritesBody.movieId)
        } else {
            addToFavorite(favoritesBody)
        }
    }

    private fun removeFavorite(id: Long) {
        viewModelScope.launch {
            firebaseUseCase.removeFavorite(token, id).handleResult(
                onComplete = {
                    isInFavorite = !isInFavorite
                    setState(MovieUiState.IsInFavorite(isInFavorite))
                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                },
                onLoading = {
                    setState(MovieUiState.Loading)
                }
            )
        }
    }

    private fun addToFavorite(favoritesBody: FavoritesBody) {
        viewModelScope.launch {
            firebaseUseCase.setFavorites(token, favoritesBody).handleResult(
                onComplete = {
                    isInFavorite = !isInFavorite
                    setState(MovieUiState.IsInFavorite(isInFavorite))
                },
                onError = {
                    setState(MovieUiState.Error(it.localizedMessage as String))
                },
                onLoading = {
                    setState(MovieUiState.Loading)
                }
            )
        }
    }




}

sealed class MovieUiState : State {

    data class Error(val message: String) : MovieUiState()

    data object Loading : MovieUiState()

    data class InitialReviews(val data: ReviewDetailsUiModel) : MovieUiState()

    data class Reviews(val data: PagingData<DetailsResultUiModel>) : MovieUiState()

    data class MovieDetails(val data: MovieDetailsUiModel) : MovieUiState()

    data class Credits(val data: CreditsUiModel) : MovieUiState()

    data class Images(val data: List<BackdropUiModel>) : MovieUiState()

    data class Videos(val data: List<VideoResultUiModel>) : MovieUiState()

    data class Recommendations(val data: PagingData<MovieUiModel>) : MovieUiState()

    data class IsInFavorite(val data: Boolean, val initialCall: Boolean = false) : MovieUiState()

}
