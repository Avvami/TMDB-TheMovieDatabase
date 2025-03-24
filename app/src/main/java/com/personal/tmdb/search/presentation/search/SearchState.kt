package com.personal.tmdb.search.presentation.search

import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.UiText

data class SearchState(
    val loading: Boolean = false,
    val searching: Boolean = false,
    val searchQuery: String = "",
    val searchType: MediaType = MediaType.MULTI,
    val searchResults: MediaResponseInfo? = null,
    val trending: MediaResponseInfo? = null,
    val popularPeople: MediaResponseInfo? = null,
    val errorMessage: UiText? = null,
    val searchErrorMessage: UiText? = null
)
