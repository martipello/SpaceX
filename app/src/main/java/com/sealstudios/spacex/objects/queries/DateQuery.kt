package com.sealstudios.spacex.objects.queries

class DateQuery{
    companion object {
        fun dateQuery(fromDate: String, toDate: String): Pair<String, Any>{
            return "date_utc" to mutableMapOf(
                "\$gte" to fromDate,
                "\$lte" to toDate
            )
        }
    }
}