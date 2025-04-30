package com.personal.tmdb.discover.presentation.discover

import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.detail.data.models.Genre
import com.personal.tmdb.detail.domain.models.GenresInfo

data class DiscoverState(
    val loading: Boolean = false,
    val uiState: MediaType,
    val showGenres: Boolean = false,
    val selectedGenre: Genre? = null,
    val genresInfo: GenresInfo? = null,
    val discover: MediaResponseInfo? = null,
    val filtersApplied: Boolean = false,
    val errorMessage: UiText? = null
)
