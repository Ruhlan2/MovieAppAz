package com.isteam.movieappaz.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.isteam.movieappaz.common.error.Error
import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.data.dto.remote.MovieDTO
import com.isteam.movieappaz.data.dto.remote.NowPlayingResponseDTO
import com.isteam.movieappaz.data.dto.remote.celebrities.CelebrityDTO
import com.isteam.movieappaz.data.dto.remote.details.reviews.DetailsResult
import com.isteam.movieappaz.data.dto.remote.details.reviews.ReviewDetailsDTO
import com.isteam.movieappaz.data.paging.CelebritiesPagingSource
import com.isteam.movieappaz.data.paging.MoviePagingSource
import com.isteam.movieappaz.data.paging.MovieReviewPagingSource
import com.isteam.movieappaz.data.service.remote.MovieService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: MovieService,
    private val gson: Gson
) {

    suspend fun getPopularMovies(): Resource<NowPlayingResponseDTO> = handleResponse {
        api.getPopularMovieApi()
    }

    suspend fun getNowPlayingMovies(): Resource<NowPlayingResponseDTO> = handleResponse {
        api.getNowPlayingMovieApi()
    }

    suspend fun getUpComingMovies(): Resource<NowPlayingResponseDTO> = handleResponse {
        api.getUpComingMovieApi()
    }

    fun getPagingMovies(
        query: String,
        typeEnum: MovieTypeEnum,
        id: Int,
        genre: Int = -1
    ): Flow<PagingData<MovieDTO>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            enablePlaceholders = true,
            jumpThreshold = Int.MIN_VALUE
        ), pagingSourceFactory = {
            MoviePagingSource(api, typeEnum, query, genre, id)
        }
    ).flow

    suspend fun getMovieDetails(id: Int) = handleResponse { api.getMovieDetails(id) }

    suspend fun getMovieCredits(id: Int) = handleResponse { api.getMovieCredits(id) }

    suspend fun getMovieImages(id: Int) = handleResponse { api.getMovieImages(id) }

    suspend fun getMovieVideos(id: Int) = handleResponse { api.getMovieVideos(id) }

    suspend fun getPeopleDetails(id:Int)=handleResponse { api.getPeopleDetails(id) }

    suspend fun getPeopleMovieCredits(id:Int)=handleResponse { api.getPeopleMovieCredits(id) }

    fun getPopularPeople(): Flow<PagingData<CelebrityDTO>> =Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            enablePlaceholders = true,
            jumpThreshold = Int.MIN_VALUE
        ), pagingSourceFactory = {
            CelebritiesPagingSource(api)
        }
    ).flow

    fun getMovieReviews(id: Int): Flow<PagingData<DetailsResult>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            enablePlaceholders = true,
            jumpThreshold = Int.MIN_VALUE
        ), pagingSourceFactory = {
            MovieReviewPagingSource(api, id)
        }
    ).flow

    suspend fun getMovieReviewsFirstPage(id: Int): Resource<ReviewDetailsDTO> = handleResponse {
        api.getMovieReviews(id, 1)
    }


    private suspend fun <T> handleResponse(responseLambda: suspend () -> Response<T>): Resource<T> {
        try {
            val response = responseLambda.invoke()

            if (response.isSuccessful) {
                response.body()?.let { return Resource.Success(it) }
            }

            val error: Error =
                gson.fromJson(
                    response.errorBody()?.charStream(),
                    Error::class.java
                )
            throw error
        } catch (e: Exception) {
            return Resource.Error(e)
        }
    }
}