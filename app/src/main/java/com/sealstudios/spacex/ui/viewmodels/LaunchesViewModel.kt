package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.LaunchesResponse
import com.sealstudios.spacex.paging.LaunchesResponsePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    val spaceXService: SpaceXService,
    private val savedStateHandle: SavedStateHandle, ) : ViewModel() {

    val launches = pagedLiveData()

    private fun pagedLiveData(): LiveData<PagingData<LaunchesResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = true,
            )
        ) {
            LaunchesResponsePagingSource(spaceXService = spaceXService)
        }.liveData.cachedIn(viewModelScope)
    }
}