package com.personal.tmdb.discover.presentation.discover

import androidx.paging.PagingData
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.detail.domain.models.Genre
import com.personal.tmdb.detail.domain.models.Genres
import kotlinx.coroutines.flow.Flow

data class DiscoverState(
    val loading: Boolean = false,
    val uiState: MediaType,
    val showGenres: Boolean = false,
    val selectedGenre: Genre? = null,
    val genres: Genres? = null,
    val discover: Flow<PagingData<MediaInfo>>? = null
)
