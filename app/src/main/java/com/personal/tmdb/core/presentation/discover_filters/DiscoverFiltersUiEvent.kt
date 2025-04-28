package com.personal.tmdb.core.presentation.discover_filters

sealed interface DiscoverFiltersUiEvent {
    data object OnNavigateBack: DiscoverFiltersUiEvent
    data object ClearAll: DiscoverFiltersUiEvent
    data class SetSelectedTab(val tab: FiltersUi): DiscoverFiltersUiEvent
    data class SetMinRating(val rating: Float): DiscoverFiltersUiEvent
    data class SetMaxRating(val rating: Float): DiscoverFiltersUiEvent
    data class SetMinVoteCount(val voteCount: Float): DiscoverFiltersUiEvent
    data object ClearRatingFilter: DiscoverFiltersUiEvent
    data class SetAirDateType(val type: AirDateType): DiscoverFiltersUiEvent
    data class SetFromAirDate(val date: String): DiscoverFiltersUiEvent
    data class SetToAirDate(val date: String): DiscoverFiltersUiEvent
    data class SetYear(val year: String): DiscoverFiltersUiEvent
    data object ClearAirDateFilter: DiscoverFiltersUiEvent
    data class SetMinRuntime(val runtime: String): DiscoverFiltersUiEvent
    data class SetMaxRuntime(val runtime: String): DiscoverFiltersUiEvent
    data object ClearRuntimeFilter: DiscoverFiltersUiEvent
    data class IncludeAdult(val adult: Boolean): DiscoverFiltersUiEvent
    data class SetContentOriginType(val type: ContentOriginType): DiscoverFiltersUiEvent
    data class SetSearchQuery(val query: String): DiscoverFiltersUiEvent
    data class SelectOrigin(val origin: String): DiscoverFiltersUiEvent
    data object ClearContentOriginFilter: DiscoverFiltersUiEvent
}