package com.sealstudios.spacex.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(
    val limit: Int,
    val page: Int,
    val pagination: Boolean = true,
    val sort: MutableMap<String, String>,
    val populate: List<String>
) :Parcelable