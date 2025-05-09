package com.personal.tmdb.home.presentation.home

import com.personal.tmdb.core.navigation.Route

sealed interface HomeUiEvent {
    data class OnNavigateTo(val route: Route): HomeUiEvent
    data object RetryRequests: HomeUiEvent
}