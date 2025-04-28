package com.personal.tmdb.core.presentation.discover

import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.discover_filters.FiltersState

sealed interface DiscoverUiEvent {
    data object OnNavigateBack: DiscoverUiEvent
    data class OnNavigateTo(val route: Route): DiscoverUiEvent
    data class SetShowGenresState(val state: Boolean): DiscoverUiEvent
    data class SetFilters(val filters: FiltersState?): DiscoverUiEvent
}