package com.sealstudios.spacex.objects

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

data class CompanyResponse(
    val name: String?,
    val founder: String?,
    val founded: Int?,
    val employees: Int?,
    val vehicles: Int?,
    val launch_sites: Int?,
    val test_sites: Int?,
    val ceo: String?,
    val cto: String?,
    val coo: String?,
    val cto_propulsion: String?,
    val valuation: BigDecimal?,
    val summary: String?,
    val id: String?,
    val headQuarters: HeadQuarters?,
    val links: Links?,
) {
    companion object {

        fun CompanyResponse.formatCompanyDescription(): String {
            return "${this.name ?: "Company"} was founded by ${this.founder ?: "Unknown"} in ${this.founded ?: "Unknown"}. " +
                    "It has now ${this.employees ?: "n/a"} employees, ${this.launch_sites ?: "n/a"} launch sites, " +
                    "and is valued at USD ${formatValuation()}"
        }

    }

    private fun formatValuation(): String {
        val decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.US)
        return DecimalFormat("#,##0.#", decimalFormatSymbols).format(this.valuation)
    }


}

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