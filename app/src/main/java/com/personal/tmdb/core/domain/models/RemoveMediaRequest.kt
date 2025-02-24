package com.personal.tmdb.core.domain.models

import com.squareup.moshi.Json

data class RemoveMedia(
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "media_id") val mediaId: Int
)

data class RemoveMediaRequest(
    val items: List<RemoveMedia>
)
