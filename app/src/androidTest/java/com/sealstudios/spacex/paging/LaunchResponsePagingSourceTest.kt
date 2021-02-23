package com.sealstudios.spacex.paging

import androidx.paging.PagingSource
import com.sealstudios.spacex.network.RetrofitSpaceXService
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.Options
import com.sealstudios.spacex.objects.PagedLaunchResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class LaunchResponsePagingSourceTest {

    @MockK
    private lateinit var retrofitSpaceXService: RetrofitSpaceXService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun load() = runBlocking {
        coEvery {
            retrofitSpaceXService.queryLaunches(
                launchQueryData = getDefaultLaunchQueryData()
            )
        } returns Response.success(
            PagedLaunchResponse(
                hasNextPage = true,
                hasPrevPage = false,
                docs = listOf(),
                totalDocs = 0,
                totalPages = 1,
                limit = 1,
                nextPage = 1,
                prevPage = 1,
                page = 1,
                offset = 0,
                pagingCounter = 1
            )
        )
        val pagingSource = LaunchResponsePagingSource(retrofitSpaceXService = retrofitSpaceXService, launchQueryData = getDefaultLaunchQueryData())
        val pagingSourceLoadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                placeholdersEnabled = false,
                loadSize = 3,
                key = 1
            )
        )
        val pagingSourceData = (pagingSourceLoadResult as? PagingSource.LoadResult.Page)?.data

        assertEquals(pagingSourceData, listOf<LaunchResponse>())

    }


    private fun getDefaultLaunchQueryData(): LaunchQueryData {
        return LaunchQueryData(
            options = Options(
                limit = 20,
                page = 1,
                sort = mutableMapOf("date_utc" to "desc"),
                populate = listOf("rocket")
            ),
            query = null
        )
    }


}