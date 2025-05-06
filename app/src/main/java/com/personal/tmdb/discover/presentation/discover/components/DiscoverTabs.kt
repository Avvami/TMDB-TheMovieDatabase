package com.personal.tmdb.discover.presentation.discover.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.ui.theme.onSurfaceDark
import com.personal.tmdb.ui.theme.onSurfaceLight

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DiscoverTabs(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    onTabSelected: ((route: Route) -> Unit)? = null,
    uiState: MediaType,
    animatedContentScope: AnimatedContentScope,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    /*Fix SharedTransitionLayout theming (bug??)*/
    val darkTheme = preferencesState().darkTheme ?: isSystemInDarkTheme()
    val chipColors = SuggestionChipDefaults.suggestionChipColors(
        containerColor = if (darkTheme) onSurfaceDark.copy(alpha = .05f) else onSurfaceLight.copy(alpha = .05f),
        labelColor = if (darkTheme) onSurfaceDark.copy(alpha = .7f) else onSurfaceLight.copy(alpha = .7f)
    )
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Row(
            modifier = modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            leadingContent?.let {
                it()
            }
            when (uiState) {
                MediaType.TV, MediaType.MOVIE -> {
                    if (uiState == MediaType.TV) {
                        SuggestionChip(
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = MediaType.TV),
                                animatedVisibilityScope = animatedContentScope
                            ),
                            enabled = false,
                            onClick = {},
                            label = {
                                Text(text = stringResource(id = R.string.tv_shows))
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                disabledLabelColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
                            ),
                            shape = CircleShape
                        )
                    } else {
                        SuggestionChip(
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = MediaType.MOVIE),
                                animatedVisibilityScope = animatedContentScope
                            ),
                            enabled = false,
                            onClick = {},
                            label = {
                                Text(text = stringResource(id = R.string.movies))
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                disabledLabelColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
                            ),
                            shape = CircleShape
                        )
                    }
                    trailingContent?.let {
                        it()
                    }
                }
                MediaType.PERSON -> {
                    SuggestionChip(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = MediaType.PERSON),
                            animatedVisibilityScope = animatedContentScope
                        ),
                        enabled = false,
                        onClick = {},
                        label = {
                            Text(text = stringResource(id = R.string.people))
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                            disabledLabelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
                        ),
                        shape = CircleShape
                    )
                }
                MediaType.UNKNOWN -> {
                    SuggestionChip(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = MediaType.TV),
                            animatedVisibilityScope = animatedContentScope,
                            renderInOverlayDuringTransition = false
                        ),
                        onClick = {
                            onTabSelected?.let {
                                it(Route.Discover(MediaType.TV.name.lowercase()))
                            }
                        },
                        label = {
                            Text(text = stringResource(id = R.string.tv_shows))
                        },
                        colors = chipColors,
                        border = BorderStroke(width = 1.dp, color = chipColors.containerColor),
                        shape = CircleShape
                    )
                    SuggestionChip(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = MediaType.MOVIE),
                            animatedVisibilityScope = animatedContentScope,
                            renderInOverlayDuringTransition = false
                        ),
                        onClick = {
                            onTabSelected?.let {
                                it(Route.Discover(MediaType.MOVIE.name.lowercase()))
                            }
                        },
                        label = {
                            Text(text = stringResource(id = R.string.movies))
                        },
                        colors = chipColors,
                        border = BorderStroke(width = 1.dp, color = chipColors.containerColor),
                        shape = CircleShape
                    )
                    SuggestionChip(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = MediaType.PERSON),
                            animatedVisibilityScope = animatedContentScope,
                            renderInOverlayDuringTransition = false
                        ),
                        onClick = {
                            onTabSelected?.let {
                                it(Route.Discover(MediaType.PERSON.name.lowercase()))
                            }
                        },
                        label = {
                            Text(text = stringResource(id = R.string.people))
                        },
                        colors = chipColors,
                        border = BorderStroke(width = 1.dp, color = chipColors.containerColor),
                        shape = CircleShape
                    )
                }
                else -> Unit
            }
        }
    }
}