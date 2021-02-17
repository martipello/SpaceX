package com.sealstudios.spacex.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Options(
    var limit: Int,
    var page: Int,
    var pagination: Boolean = true,
    var sort: Map<String, String>
) :Parcelable