package com.sealstudios.spacex.network

import com.sealstudios.spacex.objects.CompanyResponse
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.PagedLaunchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SpaceXService {

    @GET("company")
    suspend fun getCompany(): CompanyResponse

    @POST("launches/query")
    suspend fun queryLaunches(
        @Body launchQueryData: LaunchQueryData,
    ): Response<PagedLaunchResponse>

}