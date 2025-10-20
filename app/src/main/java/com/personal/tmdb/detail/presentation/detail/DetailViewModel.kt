package com.personal.tmdb.detail.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.CountryCode
import com.personal.tmdb.core.domain.util.CountryName
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.SnackbarController
import com.personal.tmdb.core.domain.util.SnackbarEvent
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
        getMediaDetails(
            mediaType = routeData.mediaType,
            mediaId = routeData.mediaId,
            appendToResponse = appendToResponse(routeData.mediaType)
        )
    }

    private fun getMediaDetails(
        mediaType: String,
        mediaId: Int,
        appendToResponse: String? = null
    ) {
        viewModelScope.launch {
            _detailState.update { it.copy(loadState = LoadState.Loading) }

            val userCountry = userRepository.getUser()?.iso31661 ?: "US"
            val languageTag = preferencesRepository.getLanguage()
            val includeImageLanguageTags = "$languageTag,en,null"
            val sessionId = userRepository.getUser()?.sessionId

            detailRepository.getMediaDetail(mediaType, mediaId, sessionId, languageTag, appendToResponse, includeImageLanguageTags)
                .onError { error ->
                    _detailState.update {
                        it.copy(loadState = LoadState.Error(error.toUiText()))
                    }
                }
                .onSuccess { result ->
                    result.belongsToCollection?.let { collection ->
                        getCollection(collectionId = collection.id)
                    }
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

    private fun getCollection(collectionId: Int, language: String? = null) {
        viewModelScope.launch {
            detailRepository.getCollection(collectionId, language)
                .onError { error ->
                    println(error)
                }
                .onSuccess { result ->
                    _detailState.update { it.copy(collection = result) }
                }
        }
    }

    private fun getAccountState(mediaType: MediaType, mediaId: Int) {
        viewModelScope.launch {
            val sessionId = userRepository.getUser()?.sessionId

            detailRepository.getAccountStates(
                mediaType = mediaType.name.lowercase(),
                mediaId = mediaId,
                sessionId = sessionId
            ).onError { error ->
                println(error.toUiText())
            }.onSuccess { result ->
                _detailState.update { it.copy(accountState = result) }
            }
        }
    }

    private fun addRating(mediaType: MediaType, mediaId: Int, rating: Int) {
        viewModelScope.launch {
            _detailState.update { it.copy(rating = true) }
            val sessionId = userRepository.getUser()?.sessionId

            userRepository.addRating(
                mediaType = mediaType.name.lowercase(),
                mediaId = mediaId,
                sessionId = sessionId ?: "",
                ratingRequest = Rated.Value(rating)
            ).onError { error ->
                SnackbarController.sendEvent(
                    event = SnackbarEvent(message = error.toUiText())
                )
                _detailState.update { it.copy(rating = false) }
            }.onSuccess {
                _detailState.update { state ->
                    val updatedAccountState = state.accountState?.copy(
                        rated = Rated.Value(rating)
                    )
                    state.copy(
                        rating = false,
                        accountState = updatedAccountState
                    )
                }
            }
        }
    }

    private fun removeRating(mediaType: MediaType, mediaId: Int) {
        viewModelScope.launch {
            _detailState.update { it.copy(rating = true) }
            val sessionId = userRepository.getUser()?.sessionId

            userRepository.removeRating(
                mediaType = mediaType.name.lowercase(),
                mediaId = mediaId,
                sessionId = sessionId ?: ""
            ).onError { error ->
                SnackbarController.sendEvent(
                    event = SnackbarEvent(message = error.toUiText())
                )
                _detailState.update { it.copy(rating = false) }
            }.onSuccess {
                _detailState.update { state ->
                    val updatedAccountState = state.accountState?.copy(
                        rated = Rated.NotRated
                    )
                    state.copy(
                        rating = false,
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
            is DetailUiEvent.GetAccountState -> {
                getAccountState(
                    mediaType = event.mediaType,
                    mediaId = event.mediaId
                )
            }
            is DetailUiEvent.SetRating -> {
                if (event.rating == 0) {
                    removeRating(event.mediaType, event.mediaId)
                } else {
                    addRating(event.mediaType, event.mediaId, event.rating)
                }
            }
            is DetailUiEvent.ShowRatingSheet -> {
                _detailState.update { it.copy(showRatingSheet = event.state) }
            }
            DetailUiEvent.RetryRequest -> Unit
            DetailUiEvent.Share -> Unit
            is DetailUiEvent.OpenYTVideo -> Unit
            is DetailUiEvent.DimTopAppBar -> {
                _detailState.update { it.copy(dimTopAppBar = event.state) }
            }
            is DetailUiEvent.OpenUrl -> Unit
            is DetailUiEvent.OpenReview -> { /*TODO*/ }
        }
    }
}