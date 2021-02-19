package com.sealstudios.spacex.objects

import java.math.BigDecimal

data class Rocket(
    val height: Height?,
    val diameter: Diameter?,
    val mass: Mass?,
    val name: String?,
    val type: String?,
    val active: Boolean?,
    val stages: Int?,
    val boosters: Int?,
    val cost_per_launch: BigDecimal?,
    val success_rate_pct: Int?,
    val first_flight: String?,
    val country: String?,
    val company: String?,
    val wikipedia: String?,
    val description: String?,
    val id: String
)

data class Height(
    val meters: Double,
    val feet: Double
)

data class Diameter(
    val meters: Double,
    val feet: Double
)

data class Mass(
    val kg: Double,
    val lb: Double
)
