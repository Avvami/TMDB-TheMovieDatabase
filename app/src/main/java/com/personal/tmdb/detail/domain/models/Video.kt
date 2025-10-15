package com.personal.tmdb.detail.domain.models

import java.time.LocalDate

data class Video(
    val id: String,
    val key: String?,
    val name: String?,
    val official: Boolean,
    val publishedAt: LocalDate?,
    val type: String?
)
