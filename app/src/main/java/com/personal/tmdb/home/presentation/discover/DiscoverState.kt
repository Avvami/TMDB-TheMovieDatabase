package com.personal.tmdb.home.presentation.discover

import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.detail.data.models.Genre

data class DiscoverState(
    val uiState: MediaType,
    val selectedGenre: Genre? = null
)
