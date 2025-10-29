package com.personal.tmdb.core.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyListsResponseDto(
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val results: List<MyListDto>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)