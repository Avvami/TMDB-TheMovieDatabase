package com.personal.tmdb.discover.presentation.discover_filters

import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.models.Language
import com.personal.tmdb.discover.domain.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DiscoverFiltersViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository
): ViewModel() {

    private val _filtersState = MutableStateFlow(FiltersState())
    val filtersState = _filtersState.asStateFlow()

    private val cleanState = FiltersState()
    private var countries: List<Country>? = null
    private var languages: List<Language>? = null
    private var originFilterJob: Job? = null

    init {
        getCountries()
        getLanguages()
    }

    private fun getCountries() {
        viewModelScope.launch {
            discoverRepository.getCountries()
                .onError { error ->
                    println(error.toString())
                }
                .onSuccess { result ->
                    countries = result
                    _filtersState.update { it.copy(countries = result) }
                }
        }
    }

    private fun getLanguages() {
        viewModelScope.launch {
            discoverRepository.getLanguages()
                .onError { error ->
                    println(error.toString())
                }
                .onSuccess { result ->
                    languages = result
                    _filtersState.update { it.copy(languages = result) }
                }
        }
    }

    private fun isRatingApplied(state: FiltersState): Boolean {
        return state.fromRating != cleanState.fromRating ||
                state.toRating != cleanState.toRating ||
                state.minimumVoteCount != cleanState.minimumVoteCount
    }

    private fun isAirDateApplied(state: FiltersState): Boolean {
        return state.fromAirDate != cleanState.fromAirDate || state.toAirDate != cleanState.toAirDate ||
                state.yearAirDate != cleanState.yearAirDate
    }

    private fun isRuntimeApplied(state: FiltersState): Boolean {
        return state.fromRuntime != cleanState.fromRuntime ||
                state.toRuntime != cleanState.toRuntime
    }

    fun filtersUiEvent(event: DiscoverFiltersUiEvent) {
        when (event) {
            DiscoverFiltersUiEvent.OnNavigateBack -> Unit
            is DiscoverFiltersUiEvent.SetSelectedTab -> {
                _filtersState.update { it.copy(filtersUi = event.tab) }
            }
            DiscoverFiltersUiEvent.ClearAll -> {
                _filtersState.update { state ->
                    FiltersState(
                        filtersUi = state.filtersUi,
                        countries = countries,
                        languages = languages
                    )
                }
            }
            DiscoverFiltersUiEvent.ClearRatingFilter -> {
                _filtersState.update {
                    it.copy(
                        ratingApplied = false,
                        fromRating = cleanState.fromRating,
                        toRating = cleanState.toRating,
                        minimumVoteCount = cleanState.minimumVoteCount
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetFromRating -> {
                _filtersState.update {
                    val state = it.copy(fromRating = event.rating)
                    state.copy(ratingApplied = isRatingApplied(state))
                }
            }
            is DiscoverFiltersUiEvent.SetToRating -> {
                _filtersState.update {
                    val state = it.copy(toRating = event.rating)
                    state.copy(ratingApplied = isRatingApplied(state))
                }
            }
            is DiscoverFiltersUiEvent.SetMinVoteCount -> {
                _filtersState.update {
                    val state = it.copy(minimumVoteCount = event.voteCount)
                    state.copy(ratingApplied = isRatingApplied(state))
                }
            }
            DiscoverFiltersUiEvent.ClearAirDateFilter -> {
                _filtersState.update {
                    it.copy(
                        airDateApplied = false,
                        fromAirDate = null,
                        toAirDate = null,
                        yearAirDate = ""
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetAirDateType -> {
                _filtersState.update { it.copy(airDateType = event.type) }
            }
            is DiscoverFiltersUiEvent.SetFromAirDate -> {
                event.dateMillis?.let { millis ->
                    val instant = Instant.ofEpochMilli(millis)
                    val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                    _filtersState.update {
                        val state = it.copy(fromAirDate = date)
                        state.copy(airDateApplied = isAirDateApplied(state))
                    }
                }
            }
            is DiscoverFiltersUiEvent.SetToAirDate -> {
                event.dateMillis?.let { millis ->
                    val instant = Instant.ofEpochMilli(millis)
                    val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                    _filtersState.update {
                        val state = it.copy(toAirDate = date)
                        state.copy(airDateApplied = isAirDateApplied(state))
                    }
                }
            }
            is DiscoverFiltersUiEvent.SetYear -> {
                event.dateMillis?.let { millis ->
                    val instant = Instant.ofEpochMilli(millis)
                    val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                    _filtersState.update {
                        val state = it.copy(yearAirDate = date.year.toString())
                        state.copy(airDateApplied = isAirDateApplied(state))
                    }
                }
            }
            DiscoverFiltersUiEvent.ClearRuntimeFilter -> {
                _filtersState.update {
                    it.copy(
                        runtimeApplied = false,
                        fromRuntime = cleanState.fromRuntime,
                        toRuntime = cleanState.toRuntime
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetFromRuntime -> {
                _filtersState.update {
                    val state = it.copy(fromRuntime = event.runtime)
                    state.copy(runtimeApplied = isRuntimeApplied(state))
                }
            }
            is DiscoverFiltersUiEvent.SetToRuntime -> {
                _filtersState.update {
                    val state = it.copy(toRuntime = event.runtime)
                    state.copy(runtimeApplied = isRuntimeApplied(state))
                }
            }
            is DiscoverFiltersUiEvent.IncludeAdult -> {
                _filtersState.update { it.copy(includeAdult = event.adult) }
            }
            DiscoverFiltersUiEvent.ClearContentOriginFilter -> {
                _filtersState.update {
                    it.copy(
                        contentOriginApplied = false,
                        selectedOrigin = null,
                        selectedOriginCode = "",
                        searchQuery = "",
                        countries = countries,
                        languages = languages
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetContentOriginType -> {
                _filtersState.update { it.copy(contentOriginType = event.type) }
            }
            is DiscoverFiltersUiEvent.SetSearchQuery -> {
                _filtersState.update { it.copy(searchQuery = event.query) }
                originFilterJob?.cancel()
                originFilterJob = viewModelScope.launch {
                    delay(150L)
                    _filtersState.update { state ->
                        state.copy(
                            countries = if (event.query.isBlank()) countries else
                                countries?.fastFilter { it.locale.displayCountry.contains(event.query, ignoreCase = true) },
                            languages = if (event.query.isBlank()) languages else
                                languages?.fastFilter { it.locale.displayLanguage.contains(event.query, ignoreCase = true) }
                        )
                    }
                }
            }
            is DiscoverFiltersUiEvent.SelectOrigin -> {
                _filtersState.update {
                    it.copy(
                        contentOriginApplied = true,
                        selectedOrigin = event.origin,
                        selectedOriginCode = when (it.contentOriginType) {
                            ContentOriginType.COUNTRY -> (event.origin as Country).code
                            ContentOriginType.LANGUAGE -> (event.origin as Language).code
                        }
                    )
                }
            }
        }
    }
}