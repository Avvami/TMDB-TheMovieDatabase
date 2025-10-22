package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseDateDto(
    @Json(name = "certification")
    val certification: String?,
    @Json(name = "iso_639_1")
    val iso6391: String?,
    @Json(name = "note")
    val note: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "type")
    val type: Int?
)