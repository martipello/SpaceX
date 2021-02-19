package com.sealstudios.spacex.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(
    var limit: Int,
    var page: Int,
    var pagination: Boolean = true,
    var sort: MutableMap<String, String>,
    var populate: List<String>
) :Parcelable