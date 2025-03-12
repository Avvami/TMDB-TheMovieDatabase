package com.personal.tmdb.core.domain.models

import com.squareup.moshi.Json

data class MediaRequest(
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "media_id") val mediaId: Int,
    @Json(name = "watchlist") val watchlist: Boolean? = null,
    @Json(name = "favorite") val favorite: Boolean? = null
)
