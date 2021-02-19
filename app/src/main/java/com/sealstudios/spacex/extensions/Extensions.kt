package com.sealstudios.spacex.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.contract

fun String.getYearForDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = dateFormat.parse(this)
    date?.let {
        val dateFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
        return dateFormatter.format(it)
    }
    return ""
}