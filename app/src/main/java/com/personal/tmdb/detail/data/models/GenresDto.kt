package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenresDto(
    @Json(name = "genres")
    val genres: List<GenreDto>?
)