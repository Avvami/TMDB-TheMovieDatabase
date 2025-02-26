package com.personal.tmdb.core.domain.models

import com.squareup.moshi.Json

data class CreateListRequest(
    val name: String,
    val description: String,
    @Json(name = "iso_3166_1") val iso31661: String,
    @Json(name = "iso_639_1") val iso6391: String,
    val public: Boolean
)
