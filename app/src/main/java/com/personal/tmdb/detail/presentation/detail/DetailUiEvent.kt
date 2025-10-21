package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.core.domain.util.CountryCode
import com.personal.tmdb.core.domain.util.CountryName
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.domain.models.Review

sealed interface DetailUiEvent {
    data object OnNavigateBack: DetailUiEvent
    data class OnNavigateTo(val route: Route): DetailUiEvent
    data class SetSelectedCountry(val selectedCountry: Map.Entry<CountryCode, CountryName>): DetailUiEvent
    data class FilterWatchCountries(val query: String): DetailUiEvent
    data class SetUiState(val state: DetailUiState): DetailUiEvent
    data class GetAccountState(val mediaType: MediaType, val mediaId: Int): DetailUiEvent
    data class SetRating(val mediaType: MediaType, val mediaId: Int, val rating: Int): DetailUiEvent
    data class ShowRatingSheet(val state: Boolean): DetailUiEvent
    data object Share: DetailUiEvent
    data object RetryRequest: DetailUiEvent
    data class OpenYTVideo(val url: String): DetailUiEvent
    data class DimTopAppBar(val state: Boolean): DetailUiEvent
    data class OpenUrl(val url: String): DetailUiEvent
    data class OpenReview(val review: Review?): DetailUiEvent
}