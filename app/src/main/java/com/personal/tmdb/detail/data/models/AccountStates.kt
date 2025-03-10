package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountStates(
    @Json(name = "favorite")
    val favorite: Boolean,
    @Json(name = "rated")
    val rated: Rated,
    @Json(name = "watchlist")
    val watchlist: Boolean
)
