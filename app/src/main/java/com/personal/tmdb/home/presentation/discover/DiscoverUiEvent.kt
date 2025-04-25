package com.personal.tmdb.home.presentation.discover

import com.personal.tmdb.core.navigation.Route

sealed interface DiscoverUiEvent {
    data object OnNavigateBack: DiscoverUiEvent
    data class OnNavigateTo(val route: Route): DiscoverUiEvent
}