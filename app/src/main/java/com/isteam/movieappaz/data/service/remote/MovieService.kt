package com.isteam.movieappaz.data.service.remote

import com.isteam.movieappaz.data.dto.remote.NowPlayingResponseDTO
import com.isteam.movieappaz.data.dto.remote.celebrities.Celebrity
import com.isteam.movieappaz.data.dto.remote.celebrities.detail.CelebrityDetailsDTO
import com.isteam.movieappaz.data.dto.remote.celebrities.movies.CelebMovieDTO
import com.isteam.movieappaz.data.dto.remote.credits.CreditsDTO
import com.isteam.movieappaz.data.dto.remote.details.images.ImagesDTO
import com.isteam.movieappaz.data.dto.remote.details.movieDetails.MovieDetailsDTO
import com.isteam.movieappaz.data.dto.remote.details.reviews.ReviewDetailsDTO
import com.isteam.movieappaz.data.dto.remote.details.videos.VideosDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovieApi(@Query("page") page: Int = 1): Response<NowPlayingResponseDTO>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovieApi(@Query("page") page: Int = 1): Response<NowPlayingResponseDTO>

    @GET("movie/upcoming")
    suspend fun getUpComingMovieApi(@Query("page") page: Int = 1): Response<NowPlayingResponseDTO>

    @GET("search/movie")
    suspend fun getSearchMoviesApi(
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<NowPlayingResponseDTO>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<MovieDetailsDTO>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: Int): Response<CreditsDTO>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en"
    ): Response<ImagesDTO>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en"
    ): Response<VideosDTO>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") id: Int,
        @Query("page") page: Int
    ): Response<NowPlayingResponseDTO>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") id: Int,
        @Query("page") page: Int,
        @Query("language") language: String = "en"
    ): Response<ReviewDetailsDTO>

    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("page") page: Int=1,
        @Query("language") language: String="en"
        ):Response<Celebrity>

    @GET("person/{person_id}")
    suspend fun getPeopleDetails(
        @Path("person_id") id:Int,
        // @Query("language") language:String="en"
    ):Response<CelebrityDetailsDTO>


    @GET("person/{person_id}/movie_credits")
    suspend fun getPeopleMovieCredits(
        @Path("person_id") id:Int,
        @Query("language") language: String="en"
    ):Response<CelebMovieDTO>

    @GET("discover/movie")
    suspend fun discoverMovie(
        @Query("with_genres") genre: Int
    ): Response<NowPlayingResponseDTO>

}