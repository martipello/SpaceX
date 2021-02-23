package com.sealstudios.spacex.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
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

fun String?.asUri(): Uri? {
    try {
        return Uri.parse(this)
    } catch (e: Exception) {}
    return null
}

fun Uri.openInBrowser(context: Context) {
    val browserIntent = Intent(Intent.ACTION_VIEW, this)
    ContextCompat.startActivity(context, browserIntent, null)
}