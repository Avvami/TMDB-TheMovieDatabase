package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesDto(
    @Json(name = "profiles")
    val profiles: List<ImageDto>?,
    @Json(name = "stills")
    val stills: List<ImageDto>?,
    @Json(name = "backdrops")
    val backdrops: List<ImageDto>?,
    @Json(name = "posters")
    val posters: List<ImageDto>?,
    @Json(name = "logos")
    val logos: List<ImageDto>?
)