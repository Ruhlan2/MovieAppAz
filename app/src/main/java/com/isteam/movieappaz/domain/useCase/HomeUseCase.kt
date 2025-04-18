package com.isteam.movieappaz.domain.useCase

import com.isteam.movieappaz.data.repository.MovieRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend fun getPopularMoviesCase() = movieRepository.getPopularMovies()

    suspend fun getNowPlayingMoviesCase() = movieRepository.getNowPlayingMovies()

    suspend fun getUpComingMoviesCase() = movieRepository.getUpComingMovies()
}