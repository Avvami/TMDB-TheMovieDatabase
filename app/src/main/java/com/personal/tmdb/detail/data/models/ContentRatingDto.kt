package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentRatingDto(
    @Json(name = "iso_3166_1")
    val iso31661: String?,
    @Json(name = "rating")
    val rating: String?
)