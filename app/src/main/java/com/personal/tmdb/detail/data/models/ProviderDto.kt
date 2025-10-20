package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProviderDto(
    @Json(name = "display_priority")
    val displayPriority: Int,
    @Json(name = "logo_path")
    val logoPath: String?,
    @Json(name = "provider_id")
    val providerId: Int,
    @Json(name = "provider_name")
    val providerName: String?
)