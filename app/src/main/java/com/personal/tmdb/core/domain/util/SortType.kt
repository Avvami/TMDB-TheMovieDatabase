package com.personal.tmdb.core.domain.util

import com.personal.tmdb.R

enum class SortType {
    POPULAR,
    RATING,
    NEWEST,
    OLDEST
}

fun sortTypeToUiText(value: SortType): UiText {
    return when (value) {
        SortType.POPULAR -> UiText.StringResource(R.string.popular)
        SortType.RATING -> UiText.StringResource(R.string.rating)
        SortType.NEWEST -> UiText.StringResource(R.string.newest)
        SortType.OLDEST -> UiText.StringResource(R.string.oldest)
    }
}

fun sortTypeToRequestString(value: SortType, mediaType: MediaType): String {
    return when (value) {
        SortType.POPULAR -> "popularity.desc"
        SortType.RATING -> "vote_average.desc"
        SortType.NEWEST -> if (mediaType == MediaType.MOVIE) "primary_release_date.desc" else "first_air_date.desc"
        SortType.OLDEST -> if (mediaType == MediaType.MOVIE) "primary_release_date.asc" else "first_air_date.asc"
    }
}