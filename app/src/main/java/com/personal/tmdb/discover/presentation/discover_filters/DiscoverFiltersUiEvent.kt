package com.personal.tmdb.discover.presentation.discover_filters

import com.personal.tmdb.core.domain.util.SortType
import com.personal.tmdb.discover.domain.models.Country

sealed interface DiscoverFiltersUiEvent {
    data object OnNavigateBack: DiscoverFiltersUiEvent
    data object ResetFilters: DiscoverFiltersUiEvent
    data object ApplyFilters: DiscoverFiltersUiEvent
    data class SetFiltersUi(val ui: FiltersUi): DiscoverFiltersUiEvent
    data class SetRating(val startRating: Float, val endRating: Float): DiscoverFiltersUiEvent
    data class SetMinVoteCount(val voteCount: String): DiscoverFiltersUiEvent
    data class ShowYearPicker(val state: Boolean): DiscoverFiltersUiEvent
    data class SetYears(val startYear: Int?, val endYear: Int?): DiscoverFiltersUiEvent
    data class SetStartRuntime(val runtime: String): DiscoverFiltersUiEvent
    data class SetEndRuntime(val runtime: String): DiscoverFiltersUiEvent
    data class IncludeAdult(val adult: Boolean): DiscoverFiltersUiEvent
    data class SetSearchQuery(val query: String): DiscoverFiltersUiEvent
    data class ApplyCountry(val country: Country?): DiscoverFiltersUiEvent
    data class SetSortType(val sortType: SortType): DiscoverFiltersUiEvent
    data class SetFilters(val filtersState: FiltersState): DiscoverFiltersUiEvent
}