package com.sealstudios.spacex.network

import com.sealstudios.spacex.objects.CompanyResponse
import com.sealstudios.spacex.objects.LaunchesResponse
import retrofit2.http.GET

interface SpaceXService {

    @GET("company")
    suspend fun getCompany(): CompanyResponse

    @GET("launches")
    suspend fun getLaunches(): LaunchesResponse

}