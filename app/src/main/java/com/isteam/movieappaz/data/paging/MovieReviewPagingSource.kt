package com.isteam.movieappaz.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.isteam.movieappaz.data.dto.remote.details.reviews.DetailsResult
import com.isteam.movieappaz.data.service.remote.MovieService

class MovieReviewPagingSource(
    private val service: MovieService,
    private val id: Int
) : PagingSource<Int, DetailsResult>() {
    override fun getRefreshKey(state: PagingState<Int, DetailsResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DetailsResult> {
        val page = params.key ?: 1

        return try {
            val response = service.getMovieReviews(
                id,
                page
            )

            LoadResult.Page(
                data = response.body()?.detailsResults.orEmpty(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.body()?.detailsResults?.isEmpty() == true) null else page.plus(
                    1
                )
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}