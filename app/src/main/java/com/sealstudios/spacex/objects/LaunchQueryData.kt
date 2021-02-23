package com.sealstudios.spacex.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class LaunchQueryData(
    var options: Options?,
    var query: MutableMap<String, @RawValue Any>?
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
            if (this.query != null){
                return this.query?.entries?.asSequence()
                    ?.filter { entry -> entry.key == DATE_UTC }
                    ?.map { it.value as Map<*, *> }
                    ?.map { it.values }
                    ?.flatten()
                    ?.map { (it as String) }
                    ?.firstOrNull()
            }
            return null
        }

        fun LaunchQueryData.removeSuccessQuery(){
            this.removeQuery(SUCCESS)
        }

        fun LaunchQueryData.addSuccessQuery(isSuccess: Boolean){
            this.addQuery(SUCCESS, isSuccess)
        }

        fun LaunchQueryData.removeDateQuery(){
            this.removeQuery(DATE_UTC)
        }

        fun LaunchQueryData.addDateQuery(dateQuery: DateQuery?){
            dateQuery?.let {
                this.addQuery(DATE_UTC, it.query)
            }
        }

        private fun LaunchQueryData.removeQuery(key: String): LaunchQueryData {
            return this.apply {
                this.query?.keys?.remove(key)
            }
        }

        private fun LaunchQueryData.addQuery(
            key: String,
            value: Any,
        ): LaunchQueryData {
            var query = this.query
            if (query != null) {
                query[key] = value
            } else {
                query = mutableMapOf(key to value)
            }
            return this.apply {
                this.query = query
            }
        }

        fun LaunchQueryData.addSortOptionAscending(){
            this.addSortOption(DATE_UTC, ASC)
        }

        fun LaunchQueryData.addSortOptionDescending(){
            this.addSortOption(DATE_UTC, DESC)
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
        private const val SUCCESS: String = "desc"
    }
}