package com.personal.tmdb.core.domain.models

data class MediaResponseInfo(
    val page: Int,
    val results: List<MediaInfo>,
    val totalPages: Int,
    val totalResults: Int,
    val paging: Boolean = false
)
