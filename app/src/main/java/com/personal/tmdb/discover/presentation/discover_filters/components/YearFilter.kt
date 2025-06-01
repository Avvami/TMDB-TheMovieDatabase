package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

@Composable
fun YearFilter(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit
) {

}