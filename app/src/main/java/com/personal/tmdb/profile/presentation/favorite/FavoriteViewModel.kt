package com.personal.tmdb.profile.presentation.favorite

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
class FavoriteViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _favoriteState = MutableStateFlow(FavoriteState())
    val favoriteState = _favoriteState.asStateFlow()

    private fun getFavorites(mediaType: MediaType, page: Int) {
        viewModelScope.launch {
            _favoriteState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }
            val language = preferencesRepository.getLanguage()
            val user = userRepository.getUser()

            userRepository.getFavorites(
                accountObjectId = user?.accountObjectId ?: "",
                mediaType = mediaType.name.lowercase(),
                sessionId = user?.sessionId ?: "",
                page = page,
                language = language
            ).onError { error ->
                _favoriteState.update {
                    it.copy(
                        loading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }.onSuccess { result ->
                _favoriteState.update {
                    it.copy(
                        loading = false,
                        favorite = result
                    )
                }
            }
        }
    }

    fun favoriteUiEvent(event: FavoriteUiEvent) {
        when (event) {
            FavoriteUiEvent.OnNavigateBack -> {}
            is FavoriteUiEvent.OnNavigateTo -> {}
            is FavoriteUiEvent.GetFavorites -> {
                getFavorites(event.mediaType, event.page)
            }
            is FavoriteUiEvent.SetMediaType -> {
                val mediaType = event.mediaType
                _favoriteState.update {
                    it.copy(
                        favorite = null,
                        mediaType = mediaType
                    )
                }
                getFavorites(mediaType, 1)
            }
        }
    }
}