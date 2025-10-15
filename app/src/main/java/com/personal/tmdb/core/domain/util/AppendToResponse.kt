package com.personal.tmdb.core.domain.util

fun appendToResponse(mediaType: String): String {
    val media = convertMediaType(mediaType)
    return when (media) {
        MediaType.TV -> listOf("account_states", "content_ratings", "aggregate_credits", "similar",
            "recommendations", "watch/providers", "reviews", "images", "videos")
        MediaType.MOVIE -> listOf("account_states", "credits", "release_dates", "similar",
            "recommendations", "watch/providers", "reviews", "images", "videos")
        MediaType.PERSON -> listOf("combined_credits", "external_ids", "images")
        MediaType.EPISODE -> listOf("images", "translations")
        else -> listOf()
    }.joinToString(",")
}