package com.sealstudios.spacex

import com.sealstudios.spacex.network.OkHttpClient
import com.sealstudios.spacex.network.ResponseHandler
import com.sealstudios.spacex.network.Status
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.network.RetrofitSpaceXService
import kotlinx.coroutines.runBlocking
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SpaceXServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: okhttp3.OkHttpClient
    private lateinit var retrofitSpaceXService: RetrofitSpaceXService
    private lateinit var spaceXService: SpaceXService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        okHttpClient = OkHttpClient(HttpLoggingInterceptor()).getOkHttpClient()

        retrofitSpaceXService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RetrofitSpaceXService::class.java)

        spaceXService = SpaceXService(retrofitSpaceXService, ResponseHandler())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getCompanyResponseSuccess() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("{ \"message\": \"SUCCESS\" }")
            )
            val response = spaceXService.getCompanyResponse()
            assert(response.status == Status.SUCCESS)
        }
    }

    @Test
    fun getCompanyResponseError() {
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(400)
                    .setBody("{ \"message\": \"ERROR\" }")
            )

            val response = spaceXService.getCompanyResponse()
            assert(response.status == Status.ERROR)
        }
    }
}