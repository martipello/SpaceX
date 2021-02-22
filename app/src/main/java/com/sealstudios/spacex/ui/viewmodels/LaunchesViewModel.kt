package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.LaunchLinks
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.paging.LaunchResponsePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val spaceXService: SpaceXService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val launchQueryData: MutableLiveData<LaunchQueryData> = getLaunchQueryDataSavedState()
    val selectedLaunchLinks: MutableLiveData<LaunchLinks> = getSelectedLaunchLinksSavedState()

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

    fun setSelectedLaunchLinks(launchLinks: LaunchLinks) {
        this.selectedLaunchLinks.value = launchLinks
        savedStateHandle.set(launchLinksKey, launchLinks)
    }

    private fun getLaunchQueryDataSavedState(): MutableLiveData<LaunchQueryData> {
        val launchQueryData =
            savedStateHandle.get<LaunchQueryData>(launchQueryDataKey) ?: getDefaultLaunchQueryData()
        return MutableLiveData(launchQueryData)
    }

    private fun getSelectedLaunchLinksSavedState(): MutableLiveData<LaunchLinks> {
        val launchLinks = savedStateHandle.get<LaunchLinks>(launchLinksKey)
        return MutableLiveData(launchLinks)
    }

    companion object {
        private const val launchQueryDataKey: String = "launchQueryDataKey"
        private const val launchLinksKey: String = "launchLinksKey"
    }

}