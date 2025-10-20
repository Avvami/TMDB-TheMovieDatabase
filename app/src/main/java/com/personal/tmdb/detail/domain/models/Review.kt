package com.personal.tmdb.detail.domain.models

import java.time.LocalDate

data class Review(
    val author: String?,
    val rating: Float?,
    val authorDetails: ReviewAuthorDetails?,
    val content: String?,
    val createdAt: LocalDate?,
    val id: String,
    val url: String?
)
