package com.sealstudios.spacex.objects

import android.os.Parcelable
import com.sealstudios.spacex.extensions.getYearForDate
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
                    .map { (it as String).getYearForDate() }
                    .toSet().toList()
        }

        fun LaunchQueryData.addQuery(
            key: String,
            value: Any,
        ): LaunchQueryData {
            var query = this.query
            if (query != null) {
                query[key] = "$value"
            } else {
                query = mutableMapOf(key to "$value")
            }
            return this.apply {
                this.query = query
            }
        }

        fun LaunchQueryData.addSortOption(
            key: String,
            value: String,
        ): LaunchQueryData {
            var sort = this.options?.sort
            if (sort != null) {
                sort[key] = value
            } else {
                sort = mutableMapOf(key to value)
            }
            return this.apply {
                this.options?.sort = sort
            }
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