package com.sealstudios.spacex.objects

data class PagedLaunchesResponse(
    val docs: List<LaunchesResponse>?,
    val totalDocs: Int?,
    val offset: Int?,
    val limit: Int?,
    val totalPages: Int?,
    val page: Int?,
    val pagingCounter: Int?,
    val hasPrevPage: Boolean?,
    val hasNextPage: Boolean?,
    val prevPage: Int?,
    val nextPage: Int?,
)


