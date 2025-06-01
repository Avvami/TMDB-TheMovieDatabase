package com.personal.tmdb.discover.presentation.discover_filters

import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.core.domain.util.fold
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverFiltersViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository
): ViewModel() {

    private val _filtersState = MutableStateFlow(FiltersState())
    val filtersState = _filtersState.asStateFlow()

    private var countries: List<Country>? = null
    private var countriesFilterJob: Job? = null

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            countries = discoverRepository.getCountries().fold(
                onSuccess = { it },
                onError = { emptyList() }
            )
            _filtersState.update { it.copy(countries = countries) }
        }
    }

    fun filtersUiEvent(event: DiscoverFiltersUiEvent) {
        when (event) {
            DiscoverFiltersUiEvent.OnNavigateBack -> Unit
            DiscoverFiltersUiEvent.ApplyFilters -> Unit
            is DiscoverFiltersUiEvent.SetFiltersUi -> {
                _filtersState.update {
                    it.copy(
                        filtersUi = event.ui,
                        searchQuery = "",
                        countries = countries
                    )
                }
            }
            DiscoverFiltersUiEvent.ResetFilters -> {
                _filtersState.update { state ->
                    FiltersState(
                        filtersUi = state.filtersUi,
                        countries = countries
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetRating -> {
                _filtersState.update {
                    it.copy(
                        startRating = event.startRating,
                        endRating = event.endRating
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetMinVoteCount -> {
                _filtersState.update { it.copy(minimumVoteCount = event.voteCount) }
            }
            is DiscoverFiltersUiEvent.ShowYearPicker -> {
                _filtersState.update { it.copy(showYearPicker = event.state) }
            }
            is DiscoverFiltersUiEvent.SetYears -> {
                _filtersState.update {
                    it.copy(
                        startYear = event.startYear,
                        endYear = event.endYear
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetStartRuntime -> {
                _filtersState.update { it.copy(startRuntime = event.runtime) }
            }
            is DiscoverFiltersUiEvent.SetEndRuntime -> {
                _filtersState.update { it.copy(endRuntime = event.runtime) }
            }
            is DiscoverFiltersUiEvent.IncludeAdult -> {
                _filtersState.update { it.copy(includeAdult = event.adult) }
            }
            is DiscoverFiltersUiEvent.SetSearchQuery -> {
                _filtersState.update { it.copy(searchQuery = event.query) }
                countriesFilterJob?.cancel()
                countriesFilterJob = viewModelScope.launch {
                    delay(150L)
                    _filtersState.update { state ->
                        state.copy(
                            countries = if (event.query.isBlank()) {
                                countries
                            } else {
                                countries?.fastFilter {
                                    it.locale.displayCountry.contains(
                                        other = event.query,
                                        ignoreCase = true
                                    )
                                }
                            }
                        )
                    }
                }
            }
            is DiscoverFiltersUiEvent.ApplyCountry -> {
                _filtersState.update {
                    it.copy(
                        filtersUi = FiltersUi.ALL,
                        selectedCountry = event.country
                    )
                }
            }
            is DiscoverFiltersUiEvent.SetSortType -> {
                _filtersState.update { it.copy(sortBy = event.sortType) }
            }
            is DiscoverFiltersUiEvent.SetFilters -> {
                val state = event.filtersState
                _filtersState.update {
                    it.copy(
                        startRating = state.startRating,
                        endRating = state.endRating,
                        minimumVoteCount = state.minimumVoteCount,
                        startYear = state.startYear,
                        endYear = state.endYear,
                        startRuntime = state.startRuntime,
                        endRuntime = state.endRuntime,
                        includeAdult = state.includeAdult
                    )
                }
            }
        }
    }
}