package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.formatYearFilter
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.core.presentation.components.CustomListItemDefaults
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState
import com.personal.tmdb.discover.presentation.discover_filters.FiltersUi

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Filters(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    with(sharedTransitionScope) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            RatingFilter(
                filtersState = filtersState,
                filtersUiEvent = filtersUiEvent
            )
            MinVoteCountFilter(
                filtersState = filtersState,
                filtersUiEvent = filtersUiEvent
            )
            CustomListItem(
                modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "Country"),
                    animatedVisibilityScope = animatedContentScope
                ),
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetFiltersUi(FiltersUi.COUNTRY))
                },
                contentPadding = PaddingValues(16.dp),
                headlineContent = {
                    Text(
                        text = stringResource(R.string.country),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                trailingContent = {
                    Text(
                        text = filtersState().selectedCountry?.locale?.displayCountry
                            ?: stringResource(R.string.all)
                    )
                },
                colors = CustomListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = MaterialTheme.shapes.medium
            )
            CustomListItem(
                modifier = modifier,
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetFiltersUi(FiltersUi.YEAR))
                },
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.year),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                trailingContent = {
                    Text(
                        text = formatYearFilter(
                            filtersState().startYear,
                            filtersState().endYear
                        )?.asString() ?: stringResource(id = R.string.all),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                },
                contentPadding = PaddingValues(16.dp),
                colors = CustomListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = MaterialTheme.shapes.medium
            )
            RuntimeFilter(
                filtersState = filtersState,
                filtersUiEvent = filtersUiEvent
            )
            CustomListItem(
                onClick = {
                    filtersUiEvent(
                        DiscoverFiltersUiEvent.IncludeAdult(
                            !filtersState().includeAdult
                        )
                    )
                },
                contentPadding = PaddingValues(16.dp),
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.include_adult),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                trailingContent = {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                    ) {
                        Switch(
                            checked = filtersState().includeAdult,
                            onCheckedChange = {
                                filtersUiEvent(
                                    DiscoverFiltersUiEvent.IncludeAdult(
                                        !filtersState().includeAdult
                                    )
                                )
                            },
                            colors = SwitchDefaults.colors(
                                uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                                uncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                uncheckedTrackColor = Color.Transparent
                            )
                        )
                    }
                },
                colors = CustomListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}