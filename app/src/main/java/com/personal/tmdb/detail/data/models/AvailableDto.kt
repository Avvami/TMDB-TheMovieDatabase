package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvailableDto(
    @Json(name = "link")
    val link: String,
    @Json(name = "ads")
    val ads: List<ProviderDto>?,
    @Json(name = "flatrate")
    val flatrate: List<ProviderDto>?,
    @Json(name = "free")
    val free: List<ProviderDto>?,
    @Json(name = "buy")
    val buy: List<ProviderDto>?,
    @Json(name = "rent")
    val rent: List<ProviderDto>?
)