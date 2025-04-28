package com.personal.tmdb.core.presentation.discover_filters

data class FiltersState(
    val filtersUi: FiltersUi = FiltersUi.RATING,
    val minimumRating: Int = 0,
    val maximumRating: Int = 10,
    val minimumVoteCount: Int = 200,
    val ratingApplied: Boolean = false,
    val airDateType: AirDateType = AirDateType.RANGE,
    val fromAirDate: String = "",
    val toAirDate: String = "",
    val yearAirDate: String = "",
    val airDateApplied: Boolean = false,
    val fromRuntime: Int = 0,
    val toRuntime: Int = 360,
    val runtimeApplied: Boolean = false,
    val includeAdult: Boolean = false,
    val contentOriginType: ContentOriginType = ContentOriginType.COUNTRY,
    val searchQuery: String = "",
    val selectedOrigin: String = "",
    val originApplied: Boolean = false
)

enum class FiltersUi {
    RATING, AIR_DATES, RUNTIME, INCLUDE_ADULT, CONTENT_ORIGIN
}

enum class AirDateType {
    RANGE, YEAR
}

enum class ContentOriginType {
    COUNTRY, LANGUAGE
}