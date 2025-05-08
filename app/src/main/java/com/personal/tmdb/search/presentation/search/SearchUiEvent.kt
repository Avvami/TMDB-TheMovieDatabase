package com.personal.tmdb.search.presentation.search

import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route

sealed interface SearchUiEvent {
    data class OnNavigateTo(val route: Route): SearchUiEvent
    data class OnSearchQueryChange(val query: String): SearchUiEvent
    data class SetSearchType(val searchType: MediaType): SearchUiEvent
    data object RetrySuggestionsRequest: SearchUiEvent
}