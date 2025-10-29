package com.personal.tmdb.discover.presentation.discover

import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.domain.models.Genre
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

sealed interface DiscoverUiEvent {
    data object OnNavigateBack: DiscoverUiEvent
    data class OnNavigateTo(val route: Route): DiscoverUiEvent
    data class SetShowGenresState(val state: Boolean): DiscoverUiEvent
    data class SetFilters(val filters: FiltersState): DiscoverUiEvent
    data class SetGenre(val genre: Genre?): DiscoverUiEvent
}