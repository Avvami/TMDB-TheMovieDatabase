package com.personal.tmdb.core.presentation

import com.personal.tmdb.core.domain.util.UiText

sealed class LoadState {
    data object NotLoading: LoadState()
    data object Loading: LoadState()
    data class Error(val errorMessage: UiText): LoadState()
    data object Success: LoadState()
}