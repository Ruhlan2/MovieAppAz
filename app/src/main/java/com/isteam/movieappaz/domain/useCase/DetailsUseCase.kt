package com.isteam.movieappaz.domain.useCase

import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.data.repository.MovieRepository
import javax.inject.Inject

class DetailsUseCase @Inject constructor(private var repo: MovieRepository) {

    suspend fun getMovieDetails(id: Int) = repo.getMovieDetails(id)

    suspend fun getMovieCredits(id: Int) = repo.getMovieCredits(id)

    suspend fun getMovieImages(id: Int) = repo.getMovieImages(id)

    suspend fun getMovieVideos(id: Int) = repo.getMovieVideos(id)

    suspend fun getMovieRecommendation(movieType: MovieTypeEnum, id: Int) =
        repo.getPagingPopularMovies(
            movieTypeEnum = movieType,
            id = id
        )

    suspend fun getMovieReviews(id: Int) = repo.getMovieReviews(id)

    suspend fun getMovieReviewsFirstPage(id: Int) = repo.getMovieReviewsFirstPage(id)

}