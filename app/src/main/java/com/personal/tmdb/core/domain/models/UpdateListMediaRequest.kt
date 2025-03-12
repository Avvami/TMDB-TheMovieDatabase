package com.personal.tmdb.core.domain.models

import com.squareup.moshi.Json

data class UpdateListMediaRequest(
    @Json(name = "items") val items: List<MediaRequest>
)
