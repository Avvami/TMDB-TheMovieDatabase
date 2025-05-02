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
import com.personal.tmdb.core.domain.util.formatAirDateRequest
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.sortTypeToRequestString
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.discover.domain.repository.DiscoverRepository
import com.personal.tmdb.discover.presentation.discover_filters.AirDateType
import com.personal.tmdb.discover.presentation.discover_filters.ContentOriginType
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState
import com.personal.tmdb.discover.presentation.discover_filters.hasChangesComparedTo
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
    private val discoverRepository: DiscoverRepository
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
            mediaType = routeData.mediaType,
            page = 1,
            filters = filtersState
        )
    }

    private fun discover(
        mediaType: String,
        page: Int,
        filters: FiltersState
    ) {
        viewModelScope.launch {
            _discoverState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }
            val language = preferencesRepository.getLanguage()

            if (convertMediaType(mediaType) == MediaType.PERSON) {
                discoverPopularPeople(language, page)
            } else {
                getGenres(mediaType, language)
                discoverMedia(mediaType, language, page, filters)
            }
        }
    }

    private suspend fun discoverMedia(
        mediaType: String,
        language: String?,
        page: Int,
        filters: FiltersState
    ) {
        discoverRepository.discoverMedia(
            mediaType = mediaType,
            language = language,
            page = page,
            includeAdult = filters.includeAdult,
            airDateYear = filters.yearAirDate.takeIf { filters.airDateType == AirDateType.YEAR } ?: "",
            fromAirDate = formatAirDateRequest(filters.fromAirDate?.takeIf { filters.airDateType == AirDateType.RANGE }),
            toAirDate = formatAirDateRequest(filters.toAirDate?.takeIf { filters.airDateType == AirDateType.RANGE }),
            sortBy = sortTypeToRequestString(_discoverState.value.sortBy, convertMediaType(mediaType)),
            fromRating = filters.fromRating.takeIf { it.isNotEmpty() }?.toFloatOrNull() ?: filters.fromRatingDefault.toFloat(),
            toRating = filters.toRating.takeIf { it.isNotEmpty() }?.toFloatOrNull() ?: filters.toRatingDefault.toFloat(),
            minRatingCount = filters.minimumVoteCount.takeIf { it.isNotEmpty() }?.toFloatOrNull() ?: filters.minimumVoteCountDefault.toFloat(),
            withGenre = _discoverState.value.selectedGenre?.id?.toString() ?: "",
            withOriginCountry = filters.selectedCountry?.code?.takeIf { filters.contentOriginType == ContentOriginType.COUNTRY } ?: "",
            withOriginalLanguage = filters.selectedLanguage?.code?.takeIf { filters.contentOriginType == ContentOriginType.LANGUAGE } ?: "",
            fromRuntime = filters.fromRuntime.takeIf { it.isNotEmpty() }?.toIntOrNull() ?: filters.fromRuntimeDefault,
            toRuntime = filters.toRuntime.takeIf { it.isNotEmpty() }?.toIntOrNull() ?: filters.toRuntimeDefault
        ).onError { error ->
            _discoverState.update {
                it.copy(
                    loading = false,
                    errorMessage = error.toUiText()
                )
            }
        }.onSuccess { result ->
            _discoverState.update {
                it.copy(
                    loading = false,
                    discover = result
                )
            }
        }
    }

    private suspend fun discoverPopularPeople(language: String?, page: Int) {
        discoverRepository.getPopularPeople(language, page)
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

    private suspend fun getGenres(mediaType: String, language: String?) {
        discoverRepository.getGenres(mediaType, language)
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

    fun discoverUiEvent(event: DiscoverUiEvent) {
        when (event) {
            DiscoverUiEvent.OnNavigateBack -> Unit
            is DiscoverUiEvent.OnNavigateTo -> Unit
            is DiscoverUiEvent.SetShowGenresState -> {
                _discoverState.update { it.copy(showGenres = event.state) }
            }
            is DiscoverUiEvent.SetFilters -> {
                val filters = event.filters
                val filtersApplied = filters.ratingApplied || filters.airDateApplied || filters.runtimeApplied
                        || filters.includeAdult || filters.contentOriginApplied
                _discoverState.update { it.copy(filtersApplied = filtersApplied) }
                if (filtersApplied && filters.hasChangesComparedTo(filtersState)) {
                    filtersState = filters
                    discover(
                        mediaType = routeData.mediaType,
                        page = 1,
                        filters = filtersState
                    )
                }
            }
            is DiscoverUiEvent.SetGenre -> {
                _discoverState.update { it.copy(selectedGenre = event.genre) }
                discover(
                    mediaType = routeData.mediaType,
                    page = 1,
                    filters = filtersState
                )
            }
        }
    }
}