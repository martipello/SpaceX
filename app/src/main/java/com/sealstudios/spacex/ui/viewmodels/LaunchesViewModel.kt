package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.sealstudios.spacex.network.SpaceXService
import com.sealstudios.spacex.objects.LaunchResponse
import com.sealstudios.spacex.paging.LaunchResponsePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val spaceXService: SpaceXService,
    private val savedStateHandle: SavedStateHandle, ) : ViewModel() {

    val launches = pagedLiveData()

    private fun pagedLiveData(): LiveData<PagingData<LaunchResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                enablePlaceholders = true,
            )
        ) {
            LaunchResponsePagingSource(spaceXService = spaceXService)
        }.liveData.cachedIn(viewModelScope)
    }
}