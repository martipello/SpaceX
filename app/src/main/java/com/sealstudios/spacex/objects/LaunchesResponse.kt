package com.sealstudios.spacex.objects

data class LaunchesResponse(
    val id: String?,
    val name: String?,
    val links: LaunchLinks?,
    val static_fire_date_utc: String?,
    val static_fire_date_unix: Long?,
    val tdb: Boolean?,
    val net: Boolean?,
    val window: Int?,
    val rocket: String?,
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
    val fairings: Fairings
)

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

data class Patch (
    val small: String?,
    val large: String?,
)

data class Reddit (
    val campaign: String?,
    val launch: String?,
    val media: String?,
    val recovery: String?,
)

data class Flikr (
    val small: List<String>,
    val original: List<String>,
)

data class Fairings (
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
