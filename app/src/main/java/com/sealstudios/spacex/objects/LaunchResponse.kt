package com.sealstudios.spacex.objects

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

data class LaunchResponse(
        val id: String?,
        val name: String?,
        val links: LaunchLinks?,
        val static_fire_date_utc: String?,
        val static_fire_date_unix: Long?,
        val tdb: Boolean?,
        val net: Boolean?,
        val window: Int?,
        val success: Boolean?,
        val failures: List<Failure>,
        val details: String?,
        val crew: List<String>,
        val ships: List<String>,
        val capsules: List<String>,
        val payloads: List<String>,
        val launchpad: String?,
        val auto_update: Boolean?,
        val flight_number: Int?,
        val date_utc: String?,
        val date_unix: Long?,
        val date_local: String?,
        val date_precision: String?,
        val upcoming: Boolean?,
        val cores: List<Core>,
        val fairings: Fairings,
        val rocket: Rocket?
) {

    companion object {

        fun getDaysSinceFromMissionLaunchText(dateString: String?): String {
            dateString?.let {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val launchDate = dateFormat.parse(dateString)
                val today = Date()
                launchDate?.let {
                    val daysDifference = getDaysDifference(it, today)
                    return if (daysDifference == 0) {
                        "Today"
                    } else {
                        "${abs(daysDifference)}"
                    }

                }
            }
            return "Unknown"
        }

        private fun getDaysDifference(fromDate: Date?, toDate: Date?): Int {
            return if (fromDate == null || toDate == null) 0 else ((toDate.time - fromDate.time) / (1000 * 60 * 60 * 24)).toInt()
        }

        fun formatDate(dateString: String?): String {
            dateString?.let {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = dateFormat.parse(dateString)
                date?.let {
                    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val parsedDateFormat = dateFormatter.format(it)
                    val parsedTimeFormat = timeFormatter.format(it)
                    return "$parsedDateFormat at $parsedTimeFormat"
                }
            }
            return "Unavailable"
        }

        fun isLaunchInTheFuture(dateString: String?): LaunchTense {
            dateString?.let {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val launchDate = dateFormat.parse(dateString)
                val today = Date()
                launchDate?.let {
                    return when {
                        it.after(today) -> {
                            LaunchTense.FUTURE
                        }
                        it.before(today) -> {
                            LaunchTense.PAST
                        }
                        it == today -> {
                            LaunchTense.NOW
                        }
                        else -> LaunchTense.UNKNOWN
                    }
                }
            }
            return LaunchTense.UNKNOWN
        }

    }

}

enum class LaunchTense {
    FUTURE, PAST, NOW, UNKNOWN
}

data class Core(
        val core: String?,
        val flight: Int?,
        val gridfins: Boolean?,
        val legs: Boolean?,
        val reused: Boolean?,
        val landing_attempt: Boolean?,
        val landing_success: Boolean?,
        val landing_type: String?,
        val landpad: String?
)

data class LaunchLinks(
        val patch: Patch?,
        val reddit: Reddit?,
        val flikr: Flikr?,
        val presskit: String?,
        val webcast: String?,
        val youtube_id: String?,
        val article: String?,
        val wikipedia: String?,
)

data class Patch(
        val small: String?,
        val large: String?,
)

data class Reddit(
        val campaign: String?,
        val launch: String?,
        val media: String?,
        val recovery: String?,
)

data class Flikr(
        val small: List<String>,
        val original: List<String>,
)

data class Fairings(
        val reused: Boolean?,
        val recovery_attempt: String?,
        val recovered: Boolean?,
        val ships: List<String>,
)

data class Failure(
        val time: Int?,
        val altitude: Int?,
        val reason: String?
)
