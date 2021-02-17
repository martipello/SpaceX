package com.sealstudios.spacex.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class LaunchQueryData (
    var options: Options?,
    var query: Map<String, @RawValue Any>?
): Parcelable