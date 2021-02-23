package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.queries.DateQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

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

        fun getAllDateFilters(): List<String> {
            return mutableListOf(
                "2016-01-01T00:00:00.000Z",
                "2017-01-01T00:00:00.000Z",
                "2018-01-01T00:00:00.000Z",
                "2019-01-01T00:00:00.000Z",
                "2020-01-01T00:00:00.000Z",
                "2021-01-01T00:00:00.000Z"
            )
        }

        fun getDateQueryForFilter(filter: String): DateQuery? {
            when (filter) {
                "2016-01-01T00:00:00.000Z" -> return DateQuery.dateQuery2016()
                "2017-01-01T00:00:00.000Z" -> return DateQuery.dateQuery2017()
                "2018-01-01T00:00:00.000Z" -> return DateQuery.dateQuery2018()
                "2019-01-01T00:00:00.000Z" -> return DateQuery.dateQuery2019()
                "2020-01-01T00:00:00.000Z" -> return DateQuery.dateQuery2020()
                "2021-01-01T00:00:00.000Z" -> return DateQuery.dateQuery2021()
            }
            return null
        }

    }

}