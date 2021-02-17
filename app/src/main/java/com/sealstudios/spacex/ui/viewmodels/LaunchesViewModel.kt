package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.objects.Options
import com.sealstudios.spacex.paging.LaunchResponsePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val spaceXService: SpaceXService,
    private val savedStateHandle: SavedStateHandle, ) : ViewModel() {

    private val launchQueryData: MutableLiveData<LaunchQueryData> = getLaunchQueryDataSavedState()

    val launches = launchQueryData.switchMap {
        pagedLiveData(it)
    }

    private fun pagedLiveData(launchQueryData: LaunchQueryData): LiveData<PagingData<LaunchResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                enablePlaceholders = true,
            )
        ) {
            LaunchResponsePagingSource(
                spaceXService = spaceXService,
                launchQueryData = launchQueryData
            )
        }.liveData.cachedIn(viewModelScope)
    }

    fun setLaunchQueryData(launchQueryData: LaunchQueryData) {
        this.launchQueryData.value = launchQueryData
        savedStateHandle.set(launchQueryDataKey, launchQueryData)
    }

    private fun getLaunchQueryDataSavedState(): MutableLiveData<LaunchQueryData> {
        val launchQueryData =
            savedStateHandle.get<LaunchQueryData>(launchQueryDataKey) ?: getDefaultLaunchQueryData()
        return MutableLiveData(launchQueryData)
    }

    private fun getDefaultLaunchQueryData(): LaunchQueryData {
        return LaunchQueryData(
            options = Options(
                limit = 20,
                page = 1,
                sort = mutableMapOf("date_utc" to "desc")
            ),
            query = null
        )
    }

    companion object {
        private const val launchQueryDataKey: String = "launchQueryDataKey"
    }

}