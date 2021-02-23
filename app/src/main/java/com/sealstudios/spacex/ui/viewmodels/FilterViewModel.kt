package com.sealstudios.spacex.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.chip.Chip
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getDefaultLaunchQueryData
import com.sealstudios.spacex.objects.DateQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addDateQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSortOptionAscending
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSortOptionDescending
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSuccessQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.removeDateQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.removeSuccessQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val launchQueryData: MutableLiveData<LaunchQueryData> = getLaunchQueryDataSavedState()

    fun onSuccessCheckedChanged(isChecked: Boolean, launchQueryData: LaunchQueryData){
        if (isChecked) {
            setLaunchQueryData(launchQueryData.addSuccessQuery(isChecked))
        } else {
            setLaunchQueryData(launchQueryData.removeSuccessQuery())
        }
    }

    fun setSortOrder(sortOrder: SortOrder, launchQueryData: LaunchQueryData){
        when(sortOrder){
            SortOrder.ASC -> setLaunchQueryData(launchQueryData.addSortOptionAscending())
            SortOrder.DESC -> setLaunchQueryData(launchQueryData.addSortOptionDescending())
        }
    }

    fun setDateQuery(
        launchQueryData: LaunchQueryData,
        filterString: String
    ) {
        getDateQueryForFilter(filterString)?.let {
            setLaunchQueryData(launchQueryData.addDateQuery(it))
        }
    }

    fun removeDateQuery(launchQueryData: LaunchQueryData) {
        setLaunchQueryData(launchQueryData.removeDateQuery())
    }

    private fun setLaunchQueryData(launchQueryData: LaunchQueryData) {
        this.launchQueryData.value = launchQueryData
        savedStateHandle.set(launchQueryDataKey, launchQueryData)
    }

    private fun getLaunchQueryDataSavedState(): MutableLiveData<LaunchQueryData> {
        val launchQueryData =
            savedStateHandle.get<LaunchQueryData>(launchQueryDataKey) ?: getDefaultLaunchQueryData()
        return MutableLiveData(launchQueryData)
    }

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

    companion object {
        private const val launchQueryDataKey: String = "launchQueryDataKey"

        fun getDateQueryForFilter(filter: String): DateQuery? {
            when (filter) {
                "2016-01-01T00:00:00.000Z" -> return DateQuery.dateQueryForYear(2016)
                "2017-01-01T00:00:00.000Z" -> return DateQuery.dateQueryForYear(2017)
                "2018-01-01T00:00:00.000Z" -> return DateQuery.dateQueryForYear(2018)
                "2019-01-01T00:00:00.000Z" -> return DateQuery.dateQueryForYear(2019)
                "2020-01-01T00:00:00.000Z" -> return DateQuery.dateQueryForYear(2020)
                "2021-01-01T00:00:00.000Z" -> return DateQuery.dateQueryForYear(2021)
            }
            return null
        }

    }

}

enum class SortOrder {
    ASC, DESC
}