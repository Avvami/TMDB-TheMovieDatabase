package com.personal.tmdb.detail.data.models

import com.personal.tmdb.core.domain.util.CountryCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WatchProvidersDto(
    @Json(name = "results")
    val results: Map<CountryCode, AvailableDto>?
)