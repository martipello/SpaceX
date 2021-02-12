package com.sealstudios.spacex.objects

data class CompanyResponse (
    val name : String?,
    val founder : String?,
    val founded : Int?,
    val employees : Int?,
    val vehicles : Int?,
    val launch_site : Int?,
    val test_sites : Int?,
    val ceo : String?,
    val cto : String?,
    val coo : String?,
    val cto_propulsion : String?,
    val valuation : Double?,
    val summary : String?,
    val id : String?,
    val headQuarters: HeadQuarters?,
    val links: Links?,
)

data class HeadQuarters(
    val address: String?,
    val city: String?,
    val state: String?,
)

data class Links(
    val website: String?,
    val flikr: String?,
    val twitter: String?,
    val elon_twitter: String?,
)