package com.sealstudios.spacex

import com.sealstudios.spacex.extensions.getYearForDate
import com.sealstudios.spacex.objects.LaunchQueryData
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.addSortOption
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.getFiltersFromQuery
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isLaunchSuccessful
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.isSortOrderAscending
import com.sealstudios.spacex.objects.LaunchQueryData.Companion.removeQuery
import com.sealstudios.spacex.objects.Options
import com.sealstudios.spacex.objects.DateQuery
import org.junit.Test


class LaunchQueryDataExtensionTests {

    @Test
    fun getYearForDate() {
        val date = "2016-01-01T00:00:00.000Z"
        val dateText = date.getYearForDate()
        assert(dateText == "2016")
    }

    @Test
    fun isSortOrderAscending() {
        val defaultLaunchQueryData = defaultLaunchQueryData()
        val isSortDescending = !defaultLaunchQueryData.isSortOrderAscending()
        assert(isSortDescending)
        val defaultLaunchQueryDataSortAscending =
            defaultLaunchQueryData.addSortOption("date_utc", "asc")
        val isSortAscending = defaultLaunchQueryDataSortAscending.isSortOrderAscending()
        assert(isSortAscending)
    }

    @Test
    fun dateFilterTest() {
        val defaultLaunchQueryData = defaultLaunchQueryData()
        defaultLaunchQueryData.addQuery(
            "date_utc",
            DateQuery.dateQuery(getDateFilters().first(), getDateFilters().last())
        )
        val filters = defaultLaunchQueryData.getFiltersFromQuery()
        assert(filters.contains(getDateFilters().first()))
        assert(filters.contains(getDateFilters().last()))
        defaultLaunchQueryData.removeQuery("date_utc")
        val emptyFilters = defaultLaunchQueryData.getFiltersFromQuery()
        assert(emptyFilters.isEmpty())
    }

    @Test
    fun launchSuccessFilterTests() {
        val defaultLaunchQueryData = defaultLaunchQueryData()
        defaultLaunchQueryData.addQuery(
            "success",
            false
        )
        assert(!defaultLaunchQueryData.isLaunchSuccessful())
        defaultLaunchQueryData.addQuery(
            "success",
            true
        )
        assert(defaultLaunchQueryData.isLaunchSuccessful())
    }

    private fun defaultLaunchQueryData(): LaunchQueryData {
        return LaunchQueryData(
            options = Options(
                limit = 20,
                page = 1,
                sort = mutableMapOf("date_utc" to "desc"),
                populate = listOf("rocket")
            ),
            query = null
        )
    }

    private fun getDateFilters(): List<String> {
        return mutableListOf(
            "2016-01-01T00:00:00.000Z",
            "2017-01-01T00:00:00.000Z",
        )
    }


}