package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.formatRuntime
import com.personal.tmdb.core.domain.util.formatTvShowRuntime
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@Composable
fun AttributeChips(
    modifier: Modifier = Modifier,
    userState: () -> UserState,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = detailState.loadState is LoadState.Loading && detailState.details == null,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { loading ->
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
        ) {
            if (loading) {
                FlowRow(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) {
                        SuggestionChip(
                            enabled = false,
                            onClick = {},
                            label = {
                                Text(text = "Meta")
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = Color.Transparent,
                                disabledLabelColor = Color.Transparent
                            ),
                            border = null,
                            shape = CircleShape
                        )
                    }
                }
            } else {
                detailState.details?.let { details ->
                    val userCountryCode = userState().user?.iso31661 ?: "US"
                    val tvShowContentRating = details.contentRatings?.contentRatingsResults
                        ?.firstOrNull { it.iso31661 == userCountryCode }?.rating?.takeIf { it.isNotEmpty() }
                    val movieContentRating = details.releaseDates?.releaseDatesResults
                        ?.firstOrNull { it.iso31661 == userCountryCode }?.releaseDates
                        ?.firstOrNull { it.certification.isNotEmpty() }?.certification?.takeIf { it.isNotEmpty() }

                    buildList<@Composable FlowRowScope.() -> Unit> {
                        details.genres?.firstOrNull()?.let { genre ->
                            add {
                                SuggestionChip(
                                    onClick = {
                                        detailUiEvent(
                                            DetailUiEvent.OnNavigateTo(
                                                Route.Discover(detailState.mediaType.name.lowercase()))
                                        )
                                    },
                                    label = {
                                        Text(text = genre.name)
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = .02f
                                        ),
                                        labelColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .02f)
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                        details.releaseDate?.let { releaseDate ->
                            add {
                                SuggestionChip(
                                    enabled = false,
                                    onClick = {},
                                    label = {
                                        Text(text = releaseDate.year.toString())
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = .02f
                                        ),
                                        disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .02f)
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                        tvShowContentRating?.let { tvShowRating ->
                            add {
                                SuggestionChip(
                                    enabled = false,
                                    onClick = {},
                                    label = {
                                        Text(text = tvShowRating)
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = .02f
                                        ),
                                        disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .02f)
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                        movieContentRating?.let { movieRating ->
                            add {
                                SuggestionChip(
                                    enabled = false,
                                    onClick = {},
                                    label = {
                                        Text(text = movieRating)
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = .02f
                                        ),
                                        disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .02f)
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                        details.runtime?.let { runtime ->
                            add {
                                SuggestionChip(
                                    enabled = false,
                                    onClick = {},
                                    label = {
                                        Text(text = formatRuntime(runtime).asString())
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = .02f
                                        ),
                                        disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .02f)
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                        if (details.numberOfSeasons != null && details.numberOfEpisodes != null) {
                            add {
                                SuggestionChip(
                                    enabled = false,
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = formatTvShowRuntime(
                                                details.numberOfSeasons,
                                                details.numberOfEpisodes
                                            )
                                        )
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = .02f
                                        ),
                                        disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .02f)
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                    }.takeIf { it.isNotEmpty() }?.let { components ->
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            components.fastForEach { component ->
                                component()
                            }
                        }
                    }
                }
            }
        }
    }
}