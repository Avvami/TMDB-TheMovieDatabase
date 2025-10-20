package com.personal.tmdb.detail.domain.models

data class ReviewsResponse(
    val page: Int,
    val results: List<Review>,
    val totalPages: Int,
    val totalResults: Int
)
