package com.sealstudios.spacex.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.ExperimentalPagingApi
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.repositories.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val spaceXRepository: SpaceXRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val launchQueryData: MutableLiveData<LaunchQueryData> = getLaunchQueryDataSavedState()

    @ExperimentalPagingApi
    val launches = launchQueryData.switchMap {
        Log.d("MEDIATOR_LOG", "launchQueryData.switchMap")
        spaceXRepository.launches(it)
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

    companion object {
        private const val launchQueryDataKey: String = "launchQueryDataKey"
    }

}