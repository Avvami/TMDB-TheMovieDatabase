package com.personal.tmdb.discover.presentation

import androidx.lifecycle.ViewModel
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DiscoverSharedViewModel: ViewModel() {

    private val _appliedFiltersState = MutableStateFlow(FiltersState())
    val appliedFiltersState = _appliedFiltersState.asStateFlow()

    fun applyFilters(filtersState: FiltersState) {
        _appliedFiltersState.update {
            it.copy(
                startRating = filtersState.startRating,
                endRating = filtersState.endRating,
                minimumVoteCount = filtersState.minimumVoteCount,
                startYear = filtersState.startYear,
                endYear = filtersState.endYear,
                startRuntime = filtersState.startRuntime,
                endRuntime = filtersState.endRuntime,
                includeAdult = filtersState.includeAdult
            )
        }
    }
}