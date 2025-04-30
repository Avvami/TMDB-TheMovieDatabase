package com.personal.tmdb.discover.presentation.discover

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.convertMediaType
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState
import com.personal.tmdb.discover.presentation.discover_filters.hasChangesComparedTo
import com.personal.tmdb.home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferencesRepository: PreferencesRepository,
    private val homeRepository: HomeRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.Discover>()

    private val _discoverState = MutableStateFlow(
        DiscoverState(
            uiState = convertMediaType(routeData.mediaType)
        )
    )
    val discoverState = _discoverState.asStateFlow()
    private var filtersState by mutableStateOf(FiltersState())

    init {
        discover(
            mediaType = convertMediaType(routeData.mediaType),
            page = 1
        )
    }

    private fun discover(
        mediaType: MediaType,
        page: Int
    ) {
        viewModelScope.launch {
            _discoverState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }
            val language = preferencesRepository.getLanguage()

            if (mediaType == MediaType.PERSON) {
                discoverPopularPeople(language, page)
            } else {
                getGenres(mediaType, language)
                /*TODO*/
            }
        }
    }

    private fun discoverPopularPeople(language: String?, page: Int) {
        viewModelScope.launch {
            homeRepository.getPopularList(MediaType.PERSON.name.lowercase(), language, page)
                .onError { error ->
                    _discoverState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _discoverState.update {
                        it.copy(
                            loading = false,
                            discover = result
                        )
                    }
                }
        }
    }

    private fun getGenres(mediaType: MediaType, language: String?) {
        viewModelScope.launch {
            homeRepository.getGenres(mediaType.name.lowercase(), language)
                .onError { error ->
                    println(error.toString())
                }
                .onSuccess { result ->
                    _discoverState.update {
                        it.copy(
                            genresInfo = result
                        )
                    }
                }
        }
    }

    fun discoverUiEvent(event: DiscoverUiEvent) {
        when (event) {
            DiscoverUiEvent.OnNavigateBack -> Unit
            is DiscoverUiEvent.OnNavigateTo -> Unit
            is DiscoverUiEvent.SetShowGenresState -> {
                _discoverState.update { it.copy(showGenres = event.state) }
            }
            is DiscoverUiEvent.SetFilters -> {
                println(event.filters)
                val filters = event.filters
                val filtersApplied = filters.ratingApplied || filters.airDateApplied || filters.runtimeApplied
                        || filters.includeAdult || filters.contentOriginApplied
                _discoverState.update { it.copy(filtersApplied = filtersApplied) }
                if (filters.hasChangesComparedTo(filtersState)) {
                    filtersState = filters
                    /*TODO: Api call*/
                }
            }
        }
    }
}