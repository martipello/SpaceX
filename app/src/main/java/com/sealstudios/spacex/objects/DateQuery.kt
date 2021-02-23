package com.sealstudios.spacex.objects

class DateQuery(
    val query: Map<String, String>
) {

    companion object {

        fun dateQuery(fromDate: String, toDate: String): DateQuery {
            return DateQuery(
                query = mutableMapOf(
                    "\$gte" to fromDate,
                    "\$lte" to toDate
                )
            )
        }

        fun dateQueryForYear(year: Int): DateQuery {
            return DateQuery(
                query = mutableMapOf(
                    "\$gte" to "$year-01-01T00:00:00.000Z",
                    "\$lte" to "${year + 1}-01-01T00:00:00.000Z"
                )
            )
        }

    }
}