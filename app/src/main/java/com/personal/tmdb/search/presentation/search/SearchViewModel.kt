package com.personal.tmdb.search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.search.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private var searchJob: Job? = null

    init {
        getTrendingList()
        getPopularPeopleList()
    }

    private fun searchFor(searchType: MediaType, query: String, page: Int) {
        searchState.value.searchResults?.let { results ->
            if (results.totalPages < page || results.paging) return
            if (results.page != page) {
                _searchState.update { state ->
                    state.copy(searchResults = state.searchResults?.copy(paging = true))
                }
            }
        }
        if (query.isBlank()) {
            _searchState.update { it.copy(searchResults = null) }
        } else {
            viewModelScope.launch {
                _searchState.update {
                    it.copy(
                        searching = page == 1,
                        errorMessage = null
                    )
                }

                val language = preferencesRepository.getLanguage()

                searchRepository.searchFor(
                    searchType = searchType.name.lowercase(),
                    query = query.trim(),
                    includeAdult = false,
                    language = language,
                    page = page
                ).onError { error ->
                    _searchState.update { state ->
                        state.copy(
                            searching = false,
                            searchResults = state.searchResults?.copy(paging = false),
                            errorMessage = error.toUiText()
                        )
                    }
                }.onSuccess { result ->
                    _searchState.update { state ->
                        val searchResults = state.searchResults
                        if (searchResults == null || page == 1) {
                            state.copy(
                                searchResults = result,
                                searching = false
                            )
                        } else {
                            val mergedResults = searchResults.results + result.results
                            val updatedSearchResults = searchResults.copy(
                                results = mergedResults,
                                page = result.page,
                                paging = false
                            )
                            state.copy(
                                searchResults = updatedSearchResults,
                                searching = false
                            )
                        }
                    }
                    if (page == 1 && result.totalPages >= 2) searchFor(searchType, query, 2)
                }
            }
        }
    }

    private fun getTrendingList() {
        viewModelScope.launch {
            _searchState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }

            val language = preferencesRepository.getLanguage()

            searchRepository.getTrendingList(TimeWindow.WEEK, language)
                .onError { error ->
                    _searchState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _searchState.update {
                        it.copy(
                            trending = result,
                            loading = false
                        )
                    }
                }
        }
    }

    private fun getPopularPeopleList() {
        viewModelScope.launch {
            _searchState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }

            val language = preferencesRepository.getLanguage()

            searchRepository.getPopularPeopleList(MediaType.PERSON.name.lowercase(), language)
                .onError { error ->
                    _searchState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _searchState.update {
                        it.copy(
                            popularPeople = result,
                            loading = false
                        )
                    }
                }
        }
    }

    fun searchUiEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnSearchQueryChange -> {
                _searchState.update { it.copy(searchQuery = event.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    searchFor(searchState.value.searchType, event.query, 1)
                }
            }
            is SearchUiEvent.SetSearchType -> {
                _searchState.update { it.copy(searchType = event.searchType) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    searchFor(event.searchType, searchState.value.searchQuery, 1)
                }
            }
            is SearchUiEvent.SearchFor -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    searchFor(event.searchType, event.query, event.page)
                }
            }
            is SearchUiEvent.OnNavigateTo -> Unit
        }
    }
}