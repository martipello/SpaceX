package com.sealstudios.spacex.objects

import com.sealstudios.spacex.objects.queries.Query

data class LaunchesQueryData (
    var options: Options?,
    var query: Map<String, Any>
)