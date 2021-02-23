package com.sealstudios.spacex.ui.viewmodels


import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sealstudios.spacex.network.RetrofitSpaceXService
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.Options
import com.sealstudios.spacex.objects.PagedLaunchResponse
import com.sealstudios.spacex.paging.LaunchResponsePagingSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class LaunchesViewModelTest {

    @MockK
    private lateinit var retrofitSpaceXService: RetrofitSpaceXService

    @MockK
    private lateinit var handle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun launches() = runBlocking {
        every { handle.get<LaunchQueryData>("launchQueryDataKey") } answers { defaultLaunchQueryData() }
        every {
            handle.set<LaunchQueryData>(
                "launchQueryDataKey",
                defaultLaunchQueryData()
            )
        } answers { defaultLaunchQueryData() }
        val launchesViewModel = LaunchesViewModel(retrofitSpaceXService = retrofitSpaceXService, handle)

        coEvery { retrofitSpaceXService.queryLaunches(launchQueryData = defaultLaunchQueryData()) } returns successResponsePagedLaunchResponse()

        withContext(Dispatchers.Main) {
            launchesViewModel.setLaunchQueryData(defaultLaunchQueryData())
        }

//        TODO find a way to test paging 3 java.lang.Exception: Method launches() should be void
//        val launchesResource = launchesViewModel.launches.getValueBlocking()

    }

    private fun successResponsePagedLaunchResponse(): Response<PagedLaunchResponse> {
        return Response.success(
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
    }

    fun launchResponsePagingSource(): LaunchResponsePagingSource {
        return LaunchResponsePagingSource(
            retrofitSpaceXService = retrofitSpaceXService,
            launchQueryData = defaultLaunchQueryData()
        )
    }

    private fun defaultLaunchQueryData(): LaunchQueryData {
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

