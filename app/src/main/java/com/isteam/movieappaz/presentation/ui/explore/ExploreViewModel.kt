package com.isteam.movieappaz.presentation.ui.explore

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.isteam.movieappaz.common.base.BaseViewModel
import com.isteam.movieappaz.common.base.State
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.Genres
import com.isteam.movieappaz.domain.model.GenreUiModel
import com.isteam.movieappaz.domain.model.MovieUiModel
import com.isteam.movieappaz.domain.model.celebrities.CelebritiesUiModel
import com.isteam.movieappaz.domain.model.celebrities.details.CelebrityDetailsUiModel
import com.isteam.movieappaz.domain.useCase.ExploreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreUseCase: ExploreUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<ExploreUiState>() {

    private val genreList = arrayListOf<GenreUiModel>()
    private var selectedIndex = -1

    fun initialCall(id:Int){
        getPeopleMovieCredits(id)
    }

    fun getExploreData() {
        getMovieListData()
        getGenres()
        getPopularPeople()
    }


    fun getLanguageCode() = sharedPreferencesManager.getLanguageCode()

    private fun getMovieListData() {
        viewModelScope.launch {
            exploreUseCase.getExploreMoviesCase().cachedIn(viewModelScope)
                .collectLatest {
                    setState(ExploreUiState.ExploreMovieList(it))
                }
        }
    }

    private fun getGenres() {
        genreList.clear()
        val list = Genres.entries.map {
            GenreUiModel(it.id, it.displayName)
        }

        genreList.addAll(list)

        setState(ExploreUiState.Genres(genreList, false))
    }

    fun changeGenre(genreUiModel: GenreUiModel) {
        val index = genreList.indexOf(genreUiModel)
        selectedIndex = if (index != selectedIndex) index else -1
        val newList = genreList.map { it.copy() }
        newList.forEachIndexed { i, it -> it.isSelected = i == selectedIndex }
        val isSelected = newList.any { it.isSelected }
        setState(ExploreUiState.Genres(newList, isSelected))
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            exploreUseCase.getSearchMoviesCase(query).cachedIn(viewModelScope).collectLatest {
                setState(ExploreUiState.ExploreMovieList(it))
            }
        }
    }

    fun getMovies() {
        viewModelScope.launch {
            exploreUseCase
                .getMovies()
                .cachedIn(viewModelScope)
                .collectLatest {
                    setState(ExploreUiState.RecommendedMovieList(it))
                }
        }
    }

    private fun getPopularPeople() {
        viewModelScope.launch {
            exploreUseCase.getPopularPeople().cachedIn(viewModelScope).collectLatest {
                setState(ExploreUiState.ExploreCelebrityList(it))
            }
        }
    }

    private fun getPeopleDetails(id:Int){
        viewModelScope.launch {
            exploreUseCase.getPeopleDetails(id).handleResult(
                onComplete = {
                    setState(ExploreUiState.PeopleDetailsList(it))
                },
                onLoading = {
                    setState(ExploreUiState.Loading)
                },
                onError = {
                    setState(ExploreUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    private fun getPeopleMovieCredits(id:Int){
        viewModelScope.launch {
            exploreUseCase.getPeopleMovieCredits(id).handleResult(
                onComplete = {
                    setState(ExploreUiState.PeopleMovieCreditsList(it.cast))
                    getPeopleDetails(id)
                },
                onLoading = {
                    setState(ExploreUiState.Loading)
                },
                onError = {
                    setState(ExploreUiState.Error(it.localizedMessage as String))
                }
            )
        }
    }

    fun discoverGenre(genre: Int) {
        viewModelScope.launch {
            exploreUseCase.discoverMovie(genre).collectLatest {
                setState(ExploreUiState.ExploreMovieList(it))
            }
        }
    }

}
sealed class ExploreUiState : State {
    data class ExploreMovieList(val list: PagingData<MovieUiModel>) : ExploreUiState()

    data class RecommendedMovieList(val list: PagingData<MovieUiModel>) : ExploreUiState()

    data class Genres(val list: List<GenreUiModel>, val isSelected: Boolean) : ExploreUiState()

    data class ExploreCelebrityList(val list: PagingData<CelebritiesUiModel>) : ExploreUiState()

    data class PeopleDetailsList(val list: CelebrityDetailsUiModel) : ExploreUiState()

    data object Loading : ExploreUiState()

    data class Error(val message: String) : ExploreUiState()

    data class PeopleMovieCreditsList(val list: List<MovieUiModel>) : ExploreUiState()


}