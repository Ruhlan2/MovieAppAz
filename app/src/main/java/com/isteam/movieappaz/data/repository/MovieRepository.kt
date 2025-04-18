package com.isteam.movieappaz.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.data.mapper.toCelebMovieUiModel
import com.isteam.movieappaz.data.mapper.toCelebrityDetailsUiModel
import com.isteam.movieappaz.data.mapper.toCelebrityProfileUiModel
import com.isteam.movieappaz.data.mapper.toCreditsUiModel
import com.isteam.movieappaz.data.mapper.toDetailsResultUiModel
import com.isteam.movieappaz.data.mapper.toImagesUiModel
import com.isteam.movieappaz.data.mapper.toListMovieUiModel
import com.isteam.movieappaz.data.mapper.toMovieDetailsUiModel
import com.isteam.movieappaz.data.mapper.toMovieUiModel
import com.isteam.movieappaz.data.mapper.toReviewDetailsUiModel
import com.isteam.movieappaz.data.mapper.toVideoUiModel
import com.isteam.movieappaz.data.source.remote.RemoteDataSource
import com.isteam.movieappaz.domain.model.MovieUiModel
import com.isteam.movieappaz.domain.model.celebrities.CelebritiesUiModel
import com.isteam.movieappaz.domain.model.celebrities.details.CelebrityDetailsUiModel
import com.isteam.movieappaz.domain.model.celebrities.movies.CelebMovieUiModel
import com.isteam.movieappaz.domain.model.credits.CreditsUiModel
import com.isteam.movieappaz.domain.model.details.images.ImagesUiModel
import com.isteam.movieappaz.domain.model.details.movieDetails.MovieDetailsUiModel
import com.isteam.movieappaz.domain.model.details.reviews.DetailsResultUiModel
import com.isteam.movieappaz.domain.model.details.reviews.ReviewDetailsUiModel
import com.isteam.movieappaz.domain.model.details.video.VideoUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getPopularMovies(): Flow<Resource<List<MovieUiModel>>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getPopularMovies()) {
            is Resource.Success -> emit(Resource.Success(response.result?.results.toListMovieUiModel()))
            is Resource.Error -> emit(Resource.Error(response.throwable))
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getNowPlayingMovies(): Flow<Resource<List<MovieUiModel>>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getNowPlayingMovies()) {
            is Resource.Success -> emit(Resource.Success(response.result?.results.toListMovieUiModel()))
            is Resource.Error -> emit(Resource.Error(response.throwable))
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getUpComingMovies(): Flow<Resource<List<MovieUiModel>>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getUpComingMovies()) {
            is Resource.Success -> emit(Resource.Success(response.result?.results.toListMovieUiModel()))
            is Resource.Error -> emit(Resource.Error(response.throwable))
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getMovieDetails(id: Int): Flow<Resource<MovieDetailsUiModel>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getMovieDetails(id)) {
            is Resource.Error -> emit(Resource.Error(response.throwable))
            Resource.Loading -> Unit
            is Resource.Success -> emit(Resource.Success(response.result.toMovieDetailsUiModel()))
        }
    }

    suspend fun getMovieCredits(id: Int): Flow<Resource<CreditsUiModel>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getMovieCredits(id)) {
            is Resource.Error -> emit(Resource.Error(response.throwable))
            Resource.Loading -> Unit
            is Resource.Success -> emit(Resource.Success(response.result.toCreditsUiModel()))
        }
    }

    suspend fun getMovieImages(id: Int): Flow<Resource<ImagesUiModel>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getMovieImages(id)) {
            is Resource.Error -> emit(Resource.Error(response.throwable))
            Resource.Loading -> Unit
            is Resource.Success -> emit(Resource.Success(response.result.toImagesUiModel()))
        }
    }

    suspend fun getPagingPopularMovies(
        movieTypeEnum: MovieTypeEnum,
        query: String = "",
        id: Int = -1,
        genre: Int = -1
    ): Flow<PagingData<MovieUiModel>> = flow {
        remoteDataSource.getPagingMovies(query, movieTypeEnum, id, genre)
            .map { it.map { it1 -> it1.toMovieUiModel() } }.collect {
                emit(it)
            }
    }.flowOn(Dispatchers.IO)

    suspend fun getMovieVideos(id: Int): Flow<Resource<VideoUiModel>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getMovieVideos(id)) {
            is Resource.Error -> emit(Resource.Error(response.throwable))
            Resource.Loading -> Unit
            is Resource.Success -> emit(Resource.Success(response.result.toVideoUiModel()))
        }
    }

    suspend fun getMovieReviews(id: Int): Flow<PagingData<DetailsResultUiModel>> = flow {
        remoteDataSource.getMovieReviews(id).map {
            it.map {
                it.toDetailsResultUiModel()
            }
        }.collect {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getMovieReviewsFirstPage(id: Int): Flow<Resource<ReviewDetailsUiModel>> = flow {
        emit(Resource.Loading)
        when (val response = remoteDataSource.getMovieReviewsFirstPage(id)) {
            is Resource.Error -> emit(Resource.Error(response.throwable))
            Resource.Loading -> Unit
            is Resource.Success -> emit(Resource.Success(response.result.toReviewDetailsUiModel()))
        }
    }


   suspend fun getPopularPeople():Flow<PagingData<CelebritiesUiModel>> = flow {
       remoteDataSource.getPopularPeople().map {
           it.map {
               it.toCelebrityProfileUiModel()
           }
       }.collect{
           emit(it)
       }
   }.flowOn(Dispatchers.IO)

    suspend fun getPeopleDetails(id:Int):Flow<Resource<CelebrityDetailsUiModel>> = flow {
       emit(Resource.Loading)
        when(val response =remoteDataSource.getPeopleDetails(id)){
            is Resource.Error->emit(Resource.Error(response.throwable))
            Resource.Loading->Unit
            is Resource.Success->emit(Resource.Success(response.result.toCelebrityDetailsUiModel()))
        }
    }

    suspend fun getPeopleMovieCredits(id:Int):Flow<Resource<CelebMovieUiModel>> = flow {
        emit(Resource.Loading)
        when(val response=remoteDataSource.getPeopleMovieCredits(id)){
            is Resource.Error->emit(Resource.Error(response.throwable))
            Resource.Loading->Unit
            is Resource.Success->emit((Resource.Success(response.result.toCelebMovieUiModel())))
        }
    }


}