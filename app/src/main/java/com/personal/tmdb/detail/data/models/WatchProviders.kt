package com.personal.tmdb.detail.data.models

import com.personal.tmdb.core.data.remote.CountryCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WatchProviders(
    @Json(name = "results")
    val watchProvidersResults: Map<CountryCode, Available>
)