package com.sealstudios.spacex.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.LaunchesQueryData
import com.sealstudios.spacex.objects.LaunchesResponse
import com.sealstudios.spacex.objects.Options

class LaunchesResponsePagingSource(private val spaceXService: SpaceXService) :
    PagingSource<Int, LaunchesResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LaunchesResponse> {
        return try {
            val currentLoadingPageKey = params.key ?: 1
            val response =
                spaceXService.queryLaunches(LaunchesQueryData(options = Options(limit = 1, page = currentLoadingPageKey)))
            val data = response.body()?.docs ?: emptyList()

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LaunchesResponse>): Int? {
        return state.anchorPosition
    }

}