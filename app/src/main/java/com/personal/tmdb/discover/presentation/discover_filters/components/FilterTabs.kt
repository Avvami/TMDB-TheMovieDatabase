package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.personal.tmdb.R
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.core.presentation.components.CustomListItemDefaults
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState
import com.personal.tmdb.discover.presentation.discover_filters.FiltersUi

@Composable
fun FilterTabs(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        CustomListItem(
            selected = filtersState().filtersUi == FiltersUi.RATING,
            onClick = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetSelectedTab(FiltersUi.RATING))
            },
            headlineContent = {
                Text(text = stringResource(id = R.string.rating))
            },
            colors = CustomListItemDefaults.colors(
                selectedContainerColor = MaterialTheme.colorScheme.surface,
                headlineColor = if (filtersState().ratingApplied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
        )
        CustomListItem(
            selected = filtersState().filtersUi == FiltersUi.AIR_DATES,
            onClick = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetSelectedTab(FiltersUi.AIR_DATES))
            },
            headlineContent = {
                Text(text = stringResource(id = R.string.air_dates))
            },
            colors = CustomListItemDefaults.colors(
                selectedContainerColor = MaterialTheme.colorScheme.surface,
                headlineColor = if (filtersState().airDateApplied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
        )
        CustomListItem(
            selected = filtersState().filtersUi == FiltersUi.RUNTIME,
            onClick = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetSelectedTab(FiltersUi.RUNTIME))
            },
            headlineContent = {
                Text(text = stringResource(id = R.string.runtime))
            },
            colors = CustomListItemDefaults.colors(
                selectedContainerColor = MaterialTheme.colorScheme.surface,
                headlineColor = if (filtersState().runtimeApplied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
        )
        CustomListItem(
            selected = filtersState().filtersUi == FiltersUi.INCLUDE_ADULT,
            onClick = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetSelectedTab(FiltersUi.INCLUDE_ADULT))
            },
            headlineContent = {
                Text(text = stringResource(id = R.string.include_adult))
            },
            colors = CustomListItemDefaults.colors(
                selectedContainerColor = MaterialTheme.colorScheme.surface,
                headlineColor = if (filtersState().includeAdult) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
        )
        AnimatedVisibility(
            visible = filtersState().countries != null && filtersState().languages != null
        ) {
            CustomListItem(
                selected = filtersState().filtersUi == FiltersUi.CONTENT_ORIGIN,
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetSelectedTab(FiltersUi.CONTENT_ORIGIN))
                },
                headlineContent = {
                    Text(text = stringResource(id = R.string.content_origin))
                },
                colors = CustomListItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.surface,
                    headlineColor = if (filtersState().contentOriginApplied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}