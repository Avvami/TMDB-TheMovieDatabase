package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route

sealed interface DetailUiEvent {
    data object OnNavigateBack: DetailUiEvent
    data class OnNavigateTo(val route: Route): DetailUiEvent
    data class SetSelectedCountry(val country: String): DetailUiEvent
    data class SetAvailableSearchQuery(val query: String): DetailUiEvent
    data object ChangeAvailableSearchState: DetailUiEvent
    data object ChangeAvailableDialogState: DetailUiEvent
    data class ShowMoreDetails(val state: Boolean): DetailUiEvent
    data class GetAccountState(val mediaType: MediaType, val mediaId: Int): DetailUiEvent
    data class SetRating(val mediaType: MediaType, val mediaId: Int, val rating: Int): DetailUiEvent
    data class ShowRatingSheet(val state: Boolean): DetailUiEvent
}