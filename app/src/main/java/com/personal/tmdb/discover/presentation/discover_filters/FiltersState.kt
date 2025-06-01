package com.personal.tmdb.discover.presentation.discover_filters

import com.personal.tmdb.core.domain.util.SortType
import com.personal.tmdb.discover.domain.models.Country

data class FiltersState(
    val filtersUi: FiltersUi = FiltersUi.ALL,
    val startRatingDefault: Int = 6,
    val endRatingDefault: Int = 10,
    val startRating: Float = startRatingDefault.toFloat(),
    val endRating: Float = endRatingDefault.toFloat(),
    val minimumVoteCountDefault: Int = 300,
    val minimumVoteCount: String = "",
    val showYearPicker: Boolean = false,
    val startYear: Int? = null,
    val endYear: Int? = null,
    val startRuntimeDefault: Int = 0,
    val endRuntimeDefault: Int = 360,
    val startRuntime: String = "",
    val endRuntime: String = "",
    val includeAdult: Boolean = false,
    val countries: List<Country>? = null,
    val searchQuery: String = "",
    val selectedCountry: Country? = null,
    val sortBy: SortType = SortType.POPULAR
)

enum class FiltersUi {
    ALL, COUNTRY, YEAR
}