package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseDatesDto(
    @Json(name = "iso_3166_1")
    val iso31661: String?,
    @Json(name = "release_dates")
    val releaseDates: List<ReleaseDateDto>?
)