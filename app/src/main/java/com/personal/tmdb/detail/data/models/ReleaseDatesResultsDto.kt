package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseDatesResultsDto(
    @Json(name = "results")
    val releaseDatesResultsDto: List<ReleaseDatesDto>?
)