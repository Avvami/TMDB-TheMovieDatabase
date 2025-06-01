package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.SortType
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

@Composable
fun SortBy(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.sort_by),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            CustomListItem(
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetSortType(SortType.POPULAR))
                },
                headlineContent = {
                    Text(
                        text = stringResource(R.string.popular),
                        color = if (filtersState().sortBy == SortType.POPULAR) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                },
                trailingContent = {
                    if (filtersState().sortBy == SortType.POPULAR) {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_checked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_unchecked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                },
                contentPadding = PaddingValues(16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
            )
            CustomListItem(
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetSortType(SortType.RATING))
                },
                headlineContent = {
                    Text(
                        text = stringResource(R.string.rating),
                        color = if (filtersState().sortBy == SortType.RATING) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                },
                trailingContent = {
                    if (filtersState().sortBy == SortType.RATING) {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_checked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_unchecked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                },
                contentPadding = PaddingValues(16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
            )
            CustomListItem(
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetSortType(SortType.NEWEST))
                },
                headlineContent = {
                    Text(
                        text = stringResource(R.string.newest),
                        color = if (filtersState().sortBy == SortType.NEWEST) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                },
                trailingContent = {
                    if (filtersState().sortBy == SortType.NEWEST) {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_checked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_unchecked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                },
                contentPadding = PaddingValues(16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
            )
            CustomListItem(
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetSortType(SortType.OLDEST))
                },
                headlineContent = {
                    Text(
                        text = stringResource(R.string.oldest),
                        color = if (filtersState().sortBy == SortType.OLDEST) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                },
                trailingContent = {
                    if (filtersState().sortBy == SortType.OLDEST) {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_checked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.icon_radio_button_unchecked_fill0_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                },
                contentPadding = PaddingValues(16.dp)
            )
        }
    }
}