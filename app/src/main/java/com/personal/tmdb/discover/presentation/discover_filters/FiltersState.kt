package com.personal.tmdb.discover.presentation.discover_filters

import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.models.Language
import java.time.LocalDate

data class FiltersState(
    val filtersUi: FiltersUi = FiltersUi.RATING,
    val fromRating: String = "",
    val fromRatingDefault: Int = 0,
    val toRating: String = "",
    val toRatingDefault: Int = 10,
    val minimumVoteCount: String = "",
    val minimumVoteCountDefault: Int = 200,
    val ratingApplied: Boolean = false,
    val airDateType: AirDateType = AirDateType.RANGE,
    val fromAirDate: LocalDate? = null,
    val toAirDate: LocalDate? = null,
    val yearAirDate: String = "",
    val airDateApplied: Boolean = false,
    val fromRuntime: String = "",
    val fromRuntimeDefault: Int = 0,
    val toRuntime: String = "",
    val toRuntimeDefault: Int = 360,
    val runtimeApplied: Boolean = false,
    val includeAdult: Boolean = false,
    val contentOriginType: ContentOriginType = ContentOriginType.COUNTRY,
    val countries: List<Country>? = null,
    val languages: List<Language>? = null,
    val searchQuery: String = "",
    val selectedCountry: Country? = null,
    val selectedLanguage: Language? = null,
    val contentOriginApplied: Boolean = false
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

fun FiltersState.hasChangesComparedTo(other: FiltersState): Boolean {
    return this.copy(
        filtersUi = other.filtersUi,
        airDateType = other.airDateType,
        contentOriginType = other.contentOriginType,
        countries = other.countries,
        languages = other.languages,
        searchQuery = other.searchQuery
    ) != other
}