package com.personal.tmdb.core.domain.util

import com.personal.tmdb.R

enum class SortType {
    POPULARITY_DESC,
    POPULARITY_ASC,
    RATING_DESC,
    RATING_ASC,
    FIRST_AIR_DATE_DESC,
    FIRST_AIR_DATE_ASC
}

fun sortTypeToUiText(value: SortType): UiText {
    return when (value) {
        SortType.POPULARITY_DESC -> UiText.StringResource(R.string.popularity_desc)
        SortType.POPULARITY_ASC -> UiText.StringResource(R.string.popularity_asc)
        SortType.RATING_DESC -> UiText.StringResource(R.string.rating_desc)
        SortType.RATING_ASC -> UiText.StringResource(R.string.rating_asc)
        SortType.FIRST_AIR_DATE_DESC -> UiText.StringResource(R.string.air_date_desc)
        SortType.FIRST_AIR_DATE_ASC -> UiText.StringResource(R.string.air_date_asc)
    }
}

fun sortTypeToRequestString(value: SortType, mediaType: MediaType): String {
    return when (value) {
        SortType.POPULARITY_DESC -> "popularity.desc"
        SortType.POPULARITY_ASC -> "popularity.asc"
        SortType.RATING_DESC -> "vote_average.desc"
        SortType.RATING_ASC -> "vote_average.asc"
        SortType.FIRST_AIR_DATE_DESC -> if (mediaType == MediaType.MOVIE) "primary_release_date.desc" else "first_air_date.desc"
        SortType.FIRST_AIR_DATE_ASC -> if (mediaType == MediaType.MOVIE) "primary_release_date.asc" else "first_air_date.asc"
    }
}