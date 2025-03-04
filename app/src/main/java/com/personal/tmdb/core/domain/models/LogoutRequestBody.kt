package com.personal.tmdb.core.domain.models

import com.squareup.moshi.Json

data class LogoutRequestBody(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "session_id") val sessionId: String
)
