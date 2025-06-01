package com.personal.tmdb.profile.presentation.watchlist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import com.personal.tmdb.core.presentation.components.IconChip
import com.personal.tmdb.core.presentation.components.IconChipDefaults
import com.personal.tmdb.profile.presentation.watchlist.WatchlistState
import com.personal.tmdb.profile.presentation.watchlist.WatchlistUiEvent

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WatchlistFilterChips(
    modifier: Modifier = Modifier,
    watchlistState: () -> WatchlistState,
    watchlistUiEvent: (WatchlistUiEvent) -> Unit
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        SharedTransitionLayout(
            modifier = modifier
        ) {
            AnimatedContent(
                targetState = watchlistState().showRecommendations,
                label = ""
            ) { showRecommendations ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showRecommendations) {
                        IconChip(
                            onClick = { watchlistUiEvent(WatchlistUiEvent.ShowRecommendations) },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(IconChipDefaults.IconSize),
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = null
                                )
                            },
                            colors = IconChipDefaults.iconChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                iconContentColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
                            ),
                            shape = CircleShape
                        )
                        SuggestionChip(
                            modifier = Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(key = "Recommendations"),
                                animatedVisibilityScope = this@AnimatedContent
                            ),
                            onClick = { watchlistUiEvent(WatchlistUiEvent.ShowRecommendations) },
                            label = { Text(text = stringResource(id = R.string.recommendations)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                labelColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
                            )
                        )
                    }
                    FilterChip(
                        selected = watchlistState().mediaType == MediaType.TV,
                        onClick = { watchlistUiEvent(WatchlistUiEvent.SetMediaType(MediaType.TV)) },
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
                        selected = watchlistState().mediaType == MediaType.MOVIE,
                        onClick = { watchlistUiEvent(WatchlistUiEvent.SetMediaType(MediaType.MOVIE)) },
                        label = { Text(text = stringResource(id = R.string.movies)) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            labelColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = null
                    )
                    if (!showRecommendations) {
                        SuggestionChip(
                            modifier = Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(key = "Recommendations"),
                                animatedVisibilityScope = this@AnimatedContent
                            ),
                            onClick = { watchlistUiEvent(WatchlistUiEvent.ShowRecommendations) },
                            label = { Text(text = stringResource(id = R.string.recommendations)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                labelColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = null
                        )
                    }
                }
            }
        }
    }
}