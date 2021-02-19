package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.Options
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
        private val savedStateHandle: SavedStateHandle, ) : ViewModel() {

    val launchQueryData: MutableLiveData<LaunchQueryData> = getLaunchQueryDataSavedState()


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