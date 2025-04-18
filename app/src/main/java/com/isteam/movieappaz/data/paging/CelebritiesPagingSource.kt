package com.isteam.movieappaz.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.isteam.movieappaz.data.dto.remote.celebrities.Celebrity
import com.isteam.movieappaz.data.dto.remote.celebrities.CelebrityDTO
import com.isteam.movieappaz.data.mapper.toCelebrityProfileUiModel
import com.isteam.movieappaz.data.service.remote.MovieService
import okio.IOException

class CelebritiesPagingSource(
    private val service:MovieService
):PagingSource<Int,CelebrityDTO>(){
    override fun getRefreshKey(state: PagingState<Int, CelebrityDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)?:
            state.closestPageToPosition(anchorPosition)?.prevKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CelebrityDTO> {
        val page=params.key?:1
        return try {
            val response=service.getPopularPeople(page)

            LoadResult.Page(
                data = response.body()?.celebrityProfile.orEmpty(),
                prevKey = if (page==1) null else page.minus(1),
                nextKey = if (response.body()?.celebrityProfile?.isEmpty() ==true) null else page.plus(1)
            )
        }catch (e:IOException){
            LoadResult.Error(e)
        }
    }


}