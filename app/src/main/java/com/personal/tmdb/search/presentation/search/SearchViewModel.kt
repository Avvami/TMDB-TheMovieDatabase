package com.personal.tmdb.search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.core.domain.util.fold
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.home.domain.models.TrendingResult
import com.personal.tmdb.search.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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
        getSearchSuggestions()
    }

    private fun searchFor(searchType: MediaType, query: String) {
        if (query.isBlank()) {
            _searchState.update { it.copy(searchResults = null) }
        } else {
            viewModelScope.launch {
                val language = preferencesRepository.getLanguage()
                val searchResults = searchRepository.searchFor(
                    searchType = searchType.name.lowercase(),
                    query = query.trim(),
                    includeAdult = false,
                    language = language
                ).flow.cachedIn(viewModelScope)
                _searchState.update { it.copy(searchResults = searchResults) }
            }
        }
    }

    private fun getSearchSuggestions() {
        viewModelScope.launch {
            _searchState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }
            val language = preferencesRepository.getLanguage()
            val trendingDeferred = async {
                searchRepository.getTrendingList(TimeWindow.WEEK, language)
                    .fold(
                        onError = { error ->
                            TrendingResult(errorMessage = error.toUiText())
                        },
                        onSuccess = { result ->
                            TrendingResult(trending = result)
                        }
                    )
            }
            val popularPeople = searchRepository.getPopularPeopleList(
                mediaType = MediaType.PERSON.name.lowercase(),
                language = language
            ).flow.cachedIn(viewModelScope)

            val trendingResult = trendingDeferred.await()

            _searchState.update {
                it.copy(
                    loading = false,
                    trending = trendingResult.trending,
                    popularPeople = popularPeople,
                    errorMessage = trendingResult.errorMessage
                )
            }
        }
    }

    fun searchUiEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnNavigateTo -> Unit
            is SearchUiEvent.OnSearchQueryChange -> {
                _searchState.update { it.copy(searchQuery = event.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    searchFor(_searchState.value.searchType, _searchState.value.searchQuery)
                }
            }
            is SearchUiEvent.SetSearchType -> {
                _searchState.update { it.copy(searchType = event.searchType) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    searchFor(_searchState.value.searchType, _searchState.value.searchQuery)
                }
            }
            SearchUiEvent.RetrySuggestionsRequest -> {
                getSearchSuggestions()
            }
        }
    }
}