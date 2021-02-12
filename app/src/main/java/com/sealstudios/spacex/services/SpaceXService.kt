package com.sealstudios.spacex.services

import com.sealstudios.spacex.objects.CompanyResponse
import retrofit2.http.GET

interface SpaceXService {

    @GET("company")
    suspend fun getCompany(): CompanyResponse

}