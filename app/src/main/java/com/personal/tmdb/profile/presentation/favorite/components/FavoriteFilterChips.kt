package com.personal.tmdb.profile.presentation.favorite.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.profile.presentation.favorite.FavoriteState
import com.personal.tmdb.profile.presentation.favorite.FavoriteUiEvent

@Composable
fun FavoriteFilterChips(
    modifier: Modifier = Modifier,
    favoriteState: () -> FavoriteState,
    favoriteUiEvent: (FavoriteUiEvent) -> Unit
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
                selected = favoriteState().mediaType == MediaType.TV,
                onClick = { favoriteUiEvent(FavoriteUiEvent.SetMediaType(MediaType.TV)) },
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
                selected = favoriteState().mediaType == MediaType.MOVIE,
                onClick = { favoriteUiEvent(FavoriteUiEvent.SetMediaType(MediaType.MOVIE)) },
                label = { Text(text = stringResource(id = R.string.movies)) },
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