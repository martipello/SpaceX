package com.sealstudios.spacex.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.sealstudios.spacex.database.SpaceXDatabase
import com.sealstudios.spacex.network.Resource
import com.sealstudios.spacex.network.ResponseHandler
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.CompanyResponse
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.paging.LaunchResponseMediator
import javax.inject.Inject


class SpaceXRepository @Inject constructor(
    private val spaceXService: SpaceXService,
    private val spaceXDatabase: SpaceXDatabase,
    private val responseHandler: ResponseHandler
) {

    suspend fun getCompanyResponse(): Resource<CompanyResponse> {
        return try {
            responseHandler.handleSuccess(spaceXService.getCompany())
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    @ExperimentalPagingApi
    fun launches(launchQueryData: LaunchQueryData): LiveData<PagingData<LaunchResponse>> {
        Log.d("MEDIATOR_LOG", "launches")
        val pagingSourceFactory = { spaceXDatabase.launchDao().getAllLaunches() }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = LaunchResponseMediator(spaceXService, spaceXDatabase, launchQueryData)
        ).liveData
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 3,
            enablePlaceholders = true,
        )
    }

}

