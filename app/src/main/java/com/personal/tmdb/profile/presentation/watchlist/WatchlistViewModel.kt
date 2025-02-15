package com.personal.tmdb.profile.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _watchlistState = MutableStateFlow(WatchlistState())
    val watchlistState = _watchlistState.asStateFlow()

    private fun getWatchlist(
        mediaType: MediaType,
        page: Int
    ) {
        viewModelScope.launch {
            _watchlistState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }
            val language = preferencesRepository.getLanguage()
            val user = userRepository.getUser()

            userRepository.getWatchlist(
                accountObjectId = user?.accountObjectId ?: "",
                mediaType = mediaType.name.lowercase(),
                sessionId = user?.sessionId ?: "",
                page = page,
                language = language
            ).onError { error ->
                println(error)
                _watchlistState.update {
                    it.copy(
                        loading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }.onSuccess { result ->
                _watchlistState.update {
                    it.copy(
                        loading = false,
                        watchlist = result
                    )
                }
            }
        }
    }

    fun watchlistUiEvent(event: WatchlistUiEvent) {
        when (event) {
            WatchlistUiEvent.OnNavigateBack -> {}
            is WatchlistUiEvent.OnNavigateTo -> {}
            is WatchlistUiEvent.GetWatchlist -> {
                getWatchlist(event.mediaType, event.page)
            }
            is WatchlistUiEvent.SetMediaType -> {
                val mediaType = event.mediaType
                _watchlistState.update {
                    it.copy(
                        watchlist = null,
                        mediaType = mediaType
                    )
                }
                getWatchlist(mediaType, 1)
            }
            WatchlistUiEvent.ShowRecommendations -> {
                _watchlistState.update { it.copy(showRecommendations = !it.showRecommendations) }
            }
        }
    }
}