package com.personal.tmdb.core.presentation.discover_filters

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DiscoverFiltersViewModel: ViewModel() {

    private val _filtersState = MutableStateFlow(FiltersState())
    val filtersState = _filtersState.asStateFlow()

    fun filtersUiEvent(event: DiscoverFiltersUiEvent) {
        when (event) {
            DiscoverFiltersUiEvent.OnNavigateBack -> Unit
            DiscoverFiltersUiEvent.ClearAll -> {
                _filtersState.update { state ->
                    FiltersState(
                        filtersUi = state.filtersUi
                    )
                }
            }
            DiscoverFiltersUiEvent.ClearAirDateFilter -> TODO()
            DiscoverFiltersUiEvent.ClearContentOriginFilter -> TODO()
            DiscoverFiltersUiEvent.ClearRatingFilter -> TODO()
            DiscoverFiltersUiEvent.ClearRuntimeFilter -> TODO()
            is DiscoverFiltersUiEvent.IncludeAdult -> TODO()
            is DiscoverFiltersUiEvent.SelectOrigin -> TODO()
            is DiscoverFiltersUiEvent.SetAirDateType -> TODO()
            is DiscoverFiltersUiEvent.SetContentOriginType -> TODO()
            is DiscoverFiltersUiEvent.SetFromAirDate -> TODO()
            is DiscoverFiltersUiEvent.SetMaxRating -> TODO()
            is DiscoverFiltersUiEvent.SetMaxRuntime -> TODO()
            is DiscoverFiltersUiEvent.SetMinRating -> TODO()
            is DiscoverFiltersUiEvent.SetMinRuntime -> TODO()
            is DiscoverFiltersUiEvent.SetMinVoteCount -> TODO()
            is DiscoverFiltersUiEvent.SetSearchQuery -> TODO()
            is DiscoverFiltersUiEvent.SetSelectedTab -> {
                _filtersState.update { it.copy(filtersUi = event.tab) }
            }
            is DiscoverFiltersUiEvent.SetToAirDate -> TODO()
            is DiscoverFiltersUiEvent.SetYear -> TODO()
        }
    }

    override fun onCleared() {
        println("ONCLEAR")
        super.onCleared()
    }
}