package com.isteam.movieappaz.domain.useCase

import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.data.repository.MovieRepository
import javax.inject.Inject

class ExploreUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend fun getExploreMoviesCase() =
        movieRepository.getPagingPopularMovies(MovieTypeEnum.POPULAR_MOVIES)

    suspend fun getSearchMoviesCase(query: String) =
        movieRepository.getPagingPopularMovies(MovieTypeEnum.SEARCH, query)

    suspend fun discoverMovie(genre: Int) = movieRepository.getPagingPopularMovies(
        MovieTypeEnum.DISCOVER,
        genre = genre
    )

    suspend fun getMovies() = movieRepository.getPagingPopularMovies(
        movieTypeEnum = MovieTypeEnum.POPULAR_MOVIES
    )
    suspend fun getPopularPeople() =movieRepository.getPopularPeople()

    suspend fun getPeopleDetails(id:Int)=movieRepository.getPeopleDetails(id)

    suspend fun getPeopleMovieCredits(id:Int)=movieRepository.getPeopleMovieCredits(id)
}