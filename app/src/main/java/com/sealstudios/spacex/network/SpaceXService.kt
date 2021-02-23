package com.sealstudios.spacex.network

import com.sealstudios.spacex.network.Resource
import com.sealstudios.spacex.network.ResponseHandler
import com.sealstudios.spacex.network.RetrofitSpaceXService
import com.sealstudios.spacex.objects.CompanyResponse
import javax.inject.Inject


class SpaceXService @Inject constructor(
    private val retrofitSpaceXService: RetrofitSpaceXService,
    private val responseHandler: ResponseHandler
) {

    suspend fun getCompanyResponse(): Resource<CompanyResponse> {
        return try {
            responseHandler.handleSuccess(retrofitSpaceXService.getCompany())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}

