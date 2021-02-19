package com.sealstudios.spacex.objects.queries

class DateQuery {
    companion object {

        // TODO come up with a more elegant solution to provide dates to filter on

        fun dateQuery(fromDate: String, toDate: String): MutableMap<String, String> {
            return mutableMapOf(
                "\$gte" to fromDate,
                "\$lte" to toDate
            )
        }

        fun dateQuery2016(): MutableMap<String, String> {
            return mutableMapOf(
                "\$gte" to "2016-01-01T00:00:00.000Z",
                "\$lte" to "2017-01-01T00:00:00.000Z"
            )
        }

        fun dateQuery2017(): MutableMap<String, String> {
            return mutableMapOf(
                "\$gte" to "2017-01-01T00:00:00.000Z",
                "\$lte" to "2018-01-01T00:00:00.000Z"
            )
        }

        fun dateQuery2018(): MutableMap<String, String> {
            return mutableMapOf(
                "\$gte" to "2018-01-01T00:00:00.000Z",
                "\$lte" to "2019-01-01T00:00:00.000Z"
            )
        }

        fun dateQuery2019(): MutableMap<String, String> {
            return mutableMapOf(
                "\$gte" to "2019-01-01T00:00:00.000Z",
                "\$lte" to "2020-01-01T00:00:00.000Z"
            )
        }

        fun dateQuery2020(): MutableMap<String, String> {
            return mutableMapOf(
                "\$gte" to "2020-01-01T00:00:00.000Z",
                "\$lte" to "2021-01-01T00:00:00.000Z"
            )
        }

        fun dateQuery2021(): MutableMap<String, String> {
            return mutableMapOf(
                "\$gte" to "2021-01-01T00:00:00.000Z",
                "\$lte" to "2022-01-01T00:00:00.000Z"
            )
        }

    }
}