package com.sealstudios.spacex.repositories

import com.sealstudios.spacex.network.Resource
import com.sealstudios.spacex.network.ResponseHandler
import com.sealstudios.spacex.objects.CompanyResponse
import com.sealstudios.spacex.services.SpaceXService
import javax.inject.Inject


class SpaceXRepository @Inject constructor(
    private val spaceXService: SpaceXService,
    private val responseHandler: ResponseHandler
) {

    suspend fun getCompanyResponse(): Resource<CompanyResponse> {
        return try {
            responseHandler.handleSuccess(spaceXService.getCompany())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}

