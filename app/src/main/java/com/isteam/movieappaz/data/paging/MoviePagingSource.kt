package com.isteam.movieappaz.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.isteam.movieappaz.common.utils.MovieTypeEnum
import com.isteam.movieappaz.data.dto.remote.MovieDTO
import com.isteam.movieappaz.data.service.remote.MovieService
import okio.IOException
import retrofit2.HttpException

class MoviePagingSource(
    private val api: MovieService,
    private val movieTypeEnum: MovieTypeEnum,
    private val query: String,
    private val genre: Int,
    private val id: Int = -1
) : PagingSource<Int, MovieDTO>() {
    override fun getRefreshKey(state: PagingState<Int, MovieDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDTO> {
        val page = params.key ?: 1

        return try {

            val response = when (movieTypeEnum) {
                MovieTypeEnum.POPULAR_MOVIES -> api.getPopularMovieApi(page)
                MovieTypeEnum.NOW_PLAYING -> api.getNowPlayingMovieApi(page)
                MovieTypeEnum.SEARCH -> api.getSearchMoviesApi(page, query)
                MovieTypeEnum.RECOMMENDATIONS -> api.getMovieRecommendations(id, page)
                MovieTypeEnum.DISCOVER -> api.discoverMovie(genre)
                else -> api.getUpComingMovieApi()
            }

            LoadResult.Page(
                data = response.body()?.results.orEmpty(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.body()?.results?.isEmpty() == true) null else page.plus(1)
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}