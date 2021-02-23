package com.sealstudios.spacex.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class LaunchQueryData(
    val options: Options?,
    val query: Map<String, @RawValue Any>?
) : Parcelable {

    companion object {

        fun LaunchQueryData.isLaunchSuccessful(): Boolean {
            val successKey =
                this.query?.filterKeys { it == SUCCESS }?.values?.firstOrNull()
            return if (successKey != null) {
                successKey as Boolean
            } else {
                false
            }
        }

        fun LaunchQueryData.isSortOrderAscending(): Boolean {
            return this.options?.sort?.entries?.first()?.value ?: DESC == ASC
        }

        fun LaunchQueryData.getFiltersFromQuery(): String? {
            if (this.query != null) {
                return this.query.entries.asSequence()
                    .filter { entry -> entry.key == DATE_UTC }
                    .map { it.value as Map<*, *> }
                    .map { it.values }
                    .flatten()
                    .map { (it as String) }
                    .firstOrNull()
            }
            return null
        }

        fun LaunchQueryData.removeSuccessQuery(): LaunchQueryData {
            return this.removeQuery(SUCCESS)
        }

        fun LaunchQueryData.addSuccessQuery(isSuccess: Boolean): LaunchQueryData {
            return this.addQuery(SUCCESS, isSuccess)
        }

        fun LaunchQueryData.removeDateQuery(): LaunchQueryData {
            return this.removeQuery(DATE_UTC)
        }

        fun LaunchQueryData.addDateQuery(dateQuery: DateQuery): LaunchQueryData {
            return this.addQuery(DATE_UTC, dateQuery.query)
        }

        private fun LaunchQueryData.removeQuery(key: String): LaunchQueryData {
            this.query ?: return this
            this.query.let {
                val queryData: MutableMap<String, Any> = mutableMapOf()
                queryData.putAll(it)
                queryData.keys.remove(key)
                return this.copy(query = queryData)
            }
        }

        private fun LaunchQueryData.addQuery(
            key: String,
            value: Any,
        ): LaunchQueryData {
            val queryData: MutableMap<String, Any> = mutableMapOf()
            this.query?.let {
                queryData.putAll(it)
            }
            queryData[key] = value
            return this.copy(query = queryData)
        }

        fun LaunchQueryData.addSortOptionAscending(): LaunchQueryData {
            return this.addSortOption(DATE_UTC, ASC)
        }

        fun LaunchQueryData.addSortOptionDescending(): LaunchQueryData {
            return this.addSortOption(DATE_UTC, DESC)
        }

        private fun LaunchQueryData.addSortOption(
            key: String,
            value: String,
        ): LaunchQueryData {
            var sort = this.options?.sort
            if (sort != null) {
                sort[key] = value
            } else {
                sort = mutableMapOf(key to value)
            }
            return this.copy(options = options?.copy(sort = sort))
        }

        fun getDefaultLaunchQueryData(): LaunchQueryData {
            return LaunchQueryData(
                options = Options(
                    limit = 20,
                    page = 1,
                    sort = mutableMapOf(DATE_UTC to DESC),
                    populate = listOf(ROCKET)
                ),
                query = null
            )
        }

        private const val DATE_UTC: String = "date_utc"
        const val ROCKET: String = "rocket"
        private const val DESC: String = "desc"
        private const val ASC: String = "asc"
        private const val SUCCESS: String = "success"
    }
}