package com.sealstudios.spacex.objects

import android.os.Parcelable
import android.util.Log
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2016
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2017
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2018
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2019
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2020
import com.sealstudios.spacex.objects.queries.DateQuery.Companion.dateQuery2021
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class LaunchQueryData(
        var options: Options?,
        var query: MutableMap<String, @RawValue Any>?
) : Parcelable {
    companion object {

        fun getLaunchSuccessFromQuery(launchQueryData: LaunchQueryData): Boolean {
            val successKey = launchQueryData.query?.filterKeys { it == "success" }?.values?.first().toString()
            return successKey.toBoolean()
        }

        fun isSortOrderAscending(launchQueryData: LaunchQueryData): Boolean {
            return launchQueryData.options?.sort?.entries?.first()?.value ?: "desc" == "asc"
        }

        fun getFiltersFromQuery(launchQueryData: Map<String, Any>): List<String> {
            return launchQueryData.entries.asSequence()
                    .filter { entry -> entry.key == "date_utc" }
                    .map { it.value as Map<*, *> }
                    .map { it.values }
                    .flatten()
                    .map { getYearForDate(it as String) }
                    .toSet().toList()
        }

        fun getAllDateFilters(): Map<String, Any> {
            return mutableMapOf(
                    dateQuery2016(),
                    dateQuery2017(),
                    dateQuery2018(),
                    dateQuery2019(),
                    dateQuery2020(),
                    dateQuery2021(),
            )
        }

        // TODO move this  make it an extension
        private fun getYearForDate(dateString: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            date?.let {
                val dateFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
                return dateFormatter.format(it)
            }
            return ""
        }

        fun getDefaultLaunchQueryData(): LaunchQueryData {
            return LaunchQueryData(
                    options = Options(
                            limit = 20,
                            page = 1,
                            sort = mutableMapOf("date_utc" to "desc")
                    ),
                    query = null
            )
        }


    }
}