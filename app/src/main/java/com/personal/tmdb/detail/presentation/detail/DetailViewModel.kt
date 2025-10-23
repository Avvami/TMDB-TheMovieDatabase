package com.personal.tmdb.detail.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.personal.tmdb.core.domain.models.MediaRequest
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.CountryCode
import com.personal.tmdb.core.domain.util.CountryName
import com.personal.tmdb.core.domain.util.ToastController
import com.personal.tmdb.core.domain.util.appendToResponse
import com.personal.tmdb.core.domain.util.convertMediaType
import com.personal.tmdb.core.domain.util.findLogoImageWithLanguage
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.data.models.Rated
import com.personal.tmdb.detail.domain.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val detailRepository: DetailRepository,
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.Detail>()

    private val _detailState = MutableStateFlow(
        DetailState(
            mediaType = convertMediaType(routeData.mediaType),
            mediaId = routeData.mediaId
        )
    )
    val detailState = _detailState.asStateFlow()

    private var watchCountries: Map<CountryCode, CountryName>? = null

    init {
        getMediaDetails()
    }

    private fun getMediaDetails() {
        viewModelScope.launch {
            _detailState.update { it.copy(loadState = LoadState.Loading) }

            val userCountry = userRepository.getUser()?.iso31661 ?: "US"
            val languageTag = preferencesRepository.getLanguage()
            val includeImageLanguageTags = "$languageTag,en,null"
            val sessionId = userRepository.getUser()?.sessionId

            detailRepository.getMediaDetail(
                mediaType = routeData.mediaType,
                mediaId = routeData.mediaId,
                sessionId = sessionId,
                language = languageTag,
                appendToResponse = appendToResponse(routeData.mediaType),
                includeImageLanguage = includeImageLanguageTags
            ).onError { error ->
                _detailState.update {
                    it.copy(loadState = LoadState.Error(error.toUiText()))
                }
            }.onSuccess { result ->
                watchCountries = result.watchCountries
                _detailState.update {
                    it.copy(
                        accountState = result.accountStates,
                        details = result,
                        logo = result.images?.findLogoImageWithLanguage(
                            preferred = languageTag,
                            fallback = result.originalLanguage
                        ),
                        watchCountries = watchCountries,
                        selectedCountry = watchCountries?.entries
                            ?.firstOrNull { (code, _) -> code == userCountry }
                            ?: watchCountries?.entries?.firstOrNull(),
                        loadState = LoadState.NotLoading
                    )
                }
            }
        }
    }

    private fun addRating(rating: Int) {
        viewModelScope.launch {
            _detailState.update { it.copy(ratingLoadState = LoadState.Loading) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""

            userRepository.addRating(
                mediaType = routeData.mediaType,
                mediaId = routeData.mediaId,
                sessionId = sessionId,
                ratingRequest = Rated.Value(rating)
            ).onError { error ->
                ToastController.sendMessage(error.toUiText())
                _detailState.update { it.copy(ratingLoadState = LoadState.NotLoading) }
            }.onSuccess {
                _detailState.update { state ->
                    val updatedAccountState = state.accountState?.copy(
                        rated = Rated.Value(rating)
                    )
                    state.copy(
                        ratingLoadState = LoadState.NotLoading,
                        accountState = updatedAccountState
                    )
                }
            }
        }
    }

    private fun removeRating() {
        viewModelScope.launch {
            _detailState.update { it.copy(ratingLoadState = LoadState.Loading) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""

            userRepository.removeRating(
                mediaType = routeData.mediaType,
                mediaId = routeData.mediaId,
                sessionId = sessionId
            ).onError { error ->
                ToastController.sendMessage(error.toUiText())
                _detailState.update { it.copy(ratingLoadState = LoadState.NotLoading) }
            }.onSuccess {
                _detailState.update { state ->
                    val updatedAccountState = state.accountState?.copy(
                        rated = Rated.NotRated
                    )
                    state.copy(
                        ratingLoadState = LoadState.NotLoading,
                        accountState = updatedAccountState
                    )
                }
            }
        }
    }

    private fun addToWatchlist(add: Boolean) {
        viewModelScope.launch {
            _detailState.update { it.copy(watchlistLoadState = LoadState.Loading) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""
            val accountId = userRepository.getUser()?.accountId ?: -1
            val mediaRequest = MediaRequest(
                mediaType = routeData.mediaType,
                mediaId = routeData.mediaId,
                watchlist = add
            )

            userRepository.updateWatchlistItem(
                accountId = accountId,
                sessionId = sessionId,
                mediaRequest = mediaRequest
            ).onError { error ->
                ToastController.sendMessage(error.toUiText())
                _detailState.update { it.copy(watchlistLoadState = LoadState.NotLoading) }
            }.onSuccess {
                _detailState.update { state ->
                    val updatedAccountState = state.accountState?.copy(watchlist = add)
                    state.copy(
                        watchlistLoadState = LoadState.NotLoading,
                        accountState = updatedAccountState
                    )
                }
            }
        }
    }

    private fun addToFavorites(add: Boolean) {
        viewModelScope.launch {
            _detailState.update { it.copy(favoriteLoadState = LoadState.Loading) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""
            val accountId = userRepository.getUser()?.accountId ?: -1
            val mediaRequest = MediaRequest(
                mediaType = routeData.mediaType,
                mediaId = routeData.mediaId,
                favorite = add
            )

            userRepository.updateFavoriteItem(
                accountId = accountId,
                sessionId = sessionId,
                mediaRequest = mediaRequest
            ).onError { error ->
                ToastController.sendMessage(error.toUiText())
                _detailState.update { it.copy(favoriteLoadState = LoadState.NotLoading) }
            }.onSuccess {
                _detailState.update { state ->
                    val updatedAccountState = state.accountState?.copy(favorite = add)
                    state.copy(
                        favoriteLoadState = LoadState.NotLoading,
                        accountState = updatedAccountState
                    )
                }
            }
        }
    }

    fun detailUiEvent(event: DetailUiEvent) {
        when (event) {
            DetailUiEvent.OnNavigateBack -> Unit
            is DetailUiEvent.OnNavigateTo -> Unit
            is DetailUiEvent.SetSelectedCountry -> {
                _detailState.update {
                    it.copy(
                        selectedCountry = event.selectedCountry,
                        watchCountries = watchCountries
                    )
                }
            }
            is DetailUiEvent.FilterWatchCountries -> {
                _detailState.update { state ->
                    state.copy(
                        watchCountries = if (event.query.isBlank()) watchCountries else
                            watchCountries?.filterValues { it.contains(event.query, ignoreCase = true) }
                    )
                }
            }
            is DetailUiEvent.SetUiState -> {
                _detailState.update { it.copy(uiState = event.state) }
            }
            is DetailUiEvent.SetRating -> {
                if (event.rating == 0) {
                    removeRating()
                } else {
                    addRating(event.rating)
                }
            }
            is DetailUiEvent.ShowRatingSheet -> {
                _detailState.update { it.copy(showRatingSheet = event.state) }
            }
            DetailUiEvent.RetryRequest -> {
                getMediaDetails()
            }
            DetailUiEvent.Share -> Unit
            is DetailUiEvent.DimTopAppBar -> {
                _detailState.update { it.copy(dimTopAppBar = event.state) }
            }
            is DetailUiEvent.OpenUrl -> Unit
            is DetailUiEvent.OpenReview -> {
                _detailState.update { it.copy(selectedReview = event.review) }
            }
            is DetailUiEvent.AddToWatchlist -> {
                addToWatchlist(event.state)
            }
            is DetailUiEvent.ShowMoreSheet -> {
                _detailState.update { it.copy(showMoreSheet = event.state) }
            }
            is DetailUiEvent.AddToFavorites -> {
                addToFavorites(event.state)
            }
        }
    }
}