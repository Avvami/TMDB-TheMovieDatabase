package com.personal.tmdb.discover.presentation.discover_filters

sealed interface DiscoverFiltersUiEvent {
    data object OnNavigateBack: DiscoverFiltersUiEvent
    data object ClearAll: DiscoverFiltersUiEvent
    data class SetSelectedTab(val tab: FiltersUi): DiscoverFiltersUiEvent
    data class SetFromRating(val rating: String): DiscoverFiltersUiEvent
    data class SetToRating(val rating: String): DiscoverFiltersUiEvent
    data class SetMinVoteCount(val voteCount: String): DiscoverFiltersUiEvent
    data object ClearRatingFilter: DiscoverFiltersUiEvent
    data class SetAirDateType(val type: AirDateType): DiscoverFiltersUiEvent
    data class SetFromAirDate(val dateMillis: Long?): DiscoverFiltersUiEvent
    data class SetToAirDate(val dateMillis: Long?): DiscoverFiltersUiEvent
    data class SetYear(val dateMillis: Long?): DiscoverFiltersUiEvent
    data object ClearAirDateFilter: DiscoverFiltersUiEvent
    data class SetFromRuntime(val runtime: String): DiscoverFiltersUiEvent
    data class SetToRuntime(val runtime: String): DiscoverFiltersUiEvent
    data object ClearRuntimeFilter: DiscoverFiltersUiEvent
    data class IncludeAdult(val adult: Boolean): DiscoverFiltersUiEvent
    data class SetContentOriginType(val type: ContentOriginType): DiscoverFiltersUiEvent
    data class SetSearchQuery(val query: String): DiscoverFiltersUiEvent
    data class SelectOrigin(val origin: Any): DiscoverFiltersUiEvent
    data object ClearContentOriginFilter: DiscoverFiltersUiEvent
}