package com.personal.tmdb.detail.data.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Credits(
    @Json(name = "cast")
    val cast: List<CastDto>?,
    @Json(name = "crew")
    val crew: List<Crew>?,
    @Json(name = "guest_stars")
    val guestStars: List<CastDto>?,
)