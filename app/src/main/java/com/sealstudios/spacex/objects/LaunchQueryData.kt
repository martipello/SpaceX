package com.sealstudios.spacex.objects

import com.sealstudios.spacex.objects.queries.Query

data class LaunchQueryData (
    var options: Options?,
    var query: Map<String, Any>?
)