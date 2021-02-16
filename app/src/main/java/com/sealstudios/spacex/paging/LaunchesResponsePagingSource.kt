package com.sealstudios.spacex.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.*
import com.sealstudios.spacex.objects.queries.DateQuery
import com.sealstudios.spacex.objects.queries.SuccessLaunchQuery

class LaunchResponsePagingSource(private val spaceXService: SpaceXService) :
    PagingSource<Int, LaunchResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LaunchResponse> {
        return try {
            val currentLoadingPageKey = params.key ?: 1
            val response =
                spaceXService.queryLaunches(
                    LaunchesQueryData(
                        options = Options(
                            limit = 20,
                            page = currentLoadingPageKey,
                            sort = mutableMapOf("date_utc" to "desc")
                        ),
                        query = mutableMapOf(
//                            DateQuery.dateQuery(fromDate = "2017-06-22T00:00:00.000Z", toDate = "2017-06-25T00:00:00.000Z"),
                            SuccessLaunchQuery.successLaunchQuery(true)
                        )
                    )
                )
            val data = response.body()?.docs ?: emptyList()

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = if (response.body()?.hasNextPage == false) null else currentLoadingPageKey + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LaunchResponse>): Int? {
        return state.anchorPosition
    }

}