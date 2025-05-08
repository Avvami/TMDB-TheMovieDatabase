package com.personal.tmdb.search.presentation.search.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.presentation.components.HorizontalSegmentedButton
import com.personal.tmdb.core.presentation.components.SegmentedButtonsRow

@Composable
fun SearchFilterTabs(
    modifier: Modifier = Modifier,
    selectedTab: MediaType,
    onSelect: (tab: MediaType) -> Unit
) {
    SegmentedButtonsRow(
        modifier = modifier,
        space = 0.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        HorizontalSegmentedButton(
            onClick = { onSelect(MediaType.MULTI) },
            selected = selectedTab == MediaType.MULTI,
            shape = MaterialTheme.shapes.small,
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                activeContentColor = MaterialTheme.colorScheme.primary,
                inactiveContainerColor = Color.Transparent,
                inactiveContentColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.all),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalSegmentedButton(
            onClick = { onSelect(MediaType.TV) },
            selected = selectedTab == MediaType.TV,
            shape = MaterialTheme.shapes.small,
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                activeContentColor = MaterialTheme.colorScheme.primary,
                inactiveContainerColor = Color.Transparent,
                inactiveContentColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.tv_shows),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalSegmentedButton(
            onClick = { onSelect(MediaType.MOVIE) },
            selected = selectedTab == MediaType.MOVIE,
            shape = MaterialTheme.shapes.small,
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                activeContentColor = MaterialTheme.colorScheme.primary,
                inactiveContainerColor = Color.Transparent,
                inactiveContentColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.movies),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalSegmentedButton(
            onClick = { onSelect(MediaType.PERSON) },
            selected = selectedTab == MediaType.PERSON,
            shape = MaterialTheme.shapes.small,
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                activeContentColor = MaterialTheme.colorScheme.primary,
                inactiveContainerColor = Color.Transparent,
                inactiveContentColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.people),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}