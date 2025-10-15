package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route

sealed interface DetailUiEvent {
    data object OnNavigateBack: DetailUiEvent
    data class OnNavigateTo(val route: Route): DetailUiEvent
    data class SetSelectedCountry(val country: String): DetailUiEvent
    data class FilterWatchCountries(val query: String): DetailUiEvent
    data class SetUiState(val state: DetailUiState): DetailUiEvent
    data class GetAccountState(val mediaType: MediaType, val mediaId: Int): DetailUiEvent
    data class SetRating(val mediaType: MediaType, val mediaId: Int, val rating: Int): DetailUiEvent
    data class ShowRatingSheet(val state: Boolean): DetailUiEvent
    data object Share: DetailUiEvent
    data object RetryRequest: DetailUiEvent
    data class OpenYTVideo(val url: String): DetailUiEvent
}