package com.personal.tmdb.core.domain.util

fun appendToResponse(mediaType: String): String {
    val media = convertMediaType(mediaType)
    return when (media) {
        MediaType.TV -> listOf("content_ratings", "aggregate_credits", "similar", "recommendations", "watch/providers", "reviews", "images")
        MediaType.MOVIE -> listOf("credits", "release_dates", "similar", "recommendations", "watch/providers", "reviews", "images")
        MediaType.PERSON -> listOf("combined_credits", "external_ids", "images")
        MediaType.EPISODE -> listOf("images", "translations")
        else -> listOf()
    }.joinToString(",")
}