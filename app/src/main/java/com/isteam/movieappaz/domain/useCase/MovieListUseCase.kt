package com.isteam.movieappaz.domain.useCase

import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.data.repository.MovieRepository
import javax.inject.Inject

class MovieListUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend fun getPopularPagingCase(movieTypeEnum: MovieTypeEnum) =
        movieRepository.getPagingPopularMovies(movieTypeEnum)

}