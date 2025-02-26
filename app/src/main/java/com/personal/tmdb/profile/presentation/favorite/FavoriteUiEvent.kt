package com.personal.tmdb.profile.presentation.favorite

import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route

sealed interface FavoriteUiEvent {
    data object OnNavigateBack: FavoriteUiEvent
    data class OnNavigateTo(val route: Route): FavoriteUiEvent
    data class GetFavorites(val mediaType: MediaType, val page: Int): FavoriteUiEvent
    data class SetMediaType(val mediaType: MediaType): FavoriteUiEvent
}