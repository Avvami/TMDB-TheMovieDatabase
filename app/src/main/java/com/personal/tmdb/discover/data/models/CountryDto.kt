package com.personal.tmdb.discover.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryDto(
    @Json(name = "english_name")
    val englishName: String?,
    @Json(name = "iso_3166_1")
    val iso31661: String
)