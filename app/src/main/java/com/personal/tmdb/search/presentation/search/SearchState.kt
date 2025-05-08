package com.personal.tmdb.search.presentation.search

import androidx.paging.PagingData
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.UiText
import kotlinx.coroutines.flow.Flow

data class SearchState(
    val loading: Boolean = false,
    val searchQuery: String = "",
    val searchType: MediaType = MediaType.MULTI,
    val searchResults: Flow<PagingData<MediaInfo>>? = null,
    val trending: MediaResponseInfo? = null,
    val popularPeople: Flow<PagingData<MediaInfo>>? = null,
    val errorMessage: UiText? = null
)
