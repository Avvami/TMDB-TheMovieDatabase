package com.personal.tmdb.search.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.search.presentation.search.SearchUiEvent

@Composable
fun SearchFilterChips(
    modifier: Modifier = Modifier,
    mediaType: () -> MediaType,
    searchUiEvent: (SearchUiEvent) -> Unit
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = mediaType() == MediaType.MULTI,
                onClick = { searchUiEvent(SearchUiEvent.SetSearchType(MediaType.MULTI)) },
                label = { Text(text = stringResource(id = R.string.all)) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    labelColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = null
            )
            FilterChip(
                selected = mediaType() == MediaType.TV,
                onClick = { searchUiEvent(SearchUiEvent.SetSearchType(MediaType.TV)) },
                label = { Text(text = stringResource(id = R.string.tv_shows)) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    labelColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = null
            )
            FilterChip(
                selected = mediaType() == MediaType.MOVIE,
                onClick = { searchUiEvent(SearchUiEvent.SetSearchType(MediaType.MOVIE)) },
                label = { Text(text = stringResource(id = R.string.movies)) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    labelColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = null
            )
            FilterChip(
                selected = mediaType() == MediaType.PERSON,
                onClick = { searchUiEvent(SearchUiEvent.SetSearchType(MediaType.PERSON)) },
                label = { Text(text = stringResource(id = R.string.people)) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    labelColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = null
            )
        }
    }
}

@Composable
fun SearchFilterChipsShimmer(
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect(),
                selected = false,
                enabled = false,
                onClick = {},
                label = { Text(text = stringResource(id = R.string.all)) },
                colors = FilterChipDefaults.filterChipColors(
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent
                ),
                border = null
            )
            FilterChip(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect(),
                selected = false,
                enabled = false,
                onClick = {},
                label = { Text(text = stringResource(id = R.string.tv_shows)) },
                colors = FilterChipDefaults.filterChipColors(
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent
                ),
                border = null
            )
            FilterChip(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect(),
                selected = false,
                enabled = false,
                onClick = {},
                label = { Text(text = stringResource(id = R.string.movies)) },
                colors = FilterChipDefaults.filterChipColors(
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent
                ),
                border = null
            )
            FilterChip(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect(),
                selected = false,
                enabled = false,
                onClick = {},
                label = { Text(text = stringResource(id = R.string.people)) },
                colors = FilterChipDefaults.filterChipColors(
                    disabledContainerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent
                ),
                border = null
            )
        }
    }
}