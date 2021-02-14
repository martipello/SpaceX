package com.sealstudios.spacex.objects

data class Options (
    var limit: Int,
    var page: Int,
    var pagination: Boolean = true,
)