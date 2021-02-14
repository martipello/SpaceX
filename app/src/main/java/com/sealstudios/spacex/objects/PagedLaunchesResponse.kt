package com.sealstudios.spacex.objects

data class PagedLaunchResponse(
    val docs: List<LaunchResponse>?,
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


