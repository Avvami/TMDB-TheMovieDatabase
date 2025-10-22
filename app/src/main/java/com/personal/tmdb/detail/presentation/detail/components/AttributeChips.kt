package com.personal.tmdb.detail.presentation.detail.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.formatRuntime
import com.personal.tmdb.core.domain.util.formatTvShowRuntime
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.onSurfaceDark
import com.personal.tmdb.ui.theme.surfaceVariantDark

@Composable
fun AttributeChips(
    modifier: Modifier = Modifier,
    userState: () -> UserState,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        detailState.details?.let { details ->
            val userCountryCode = userState().user?.iso31661 ?: "US"
            val contentRating = remember {
                when (detailState.mediaType) {
                    MediaType.TV -> {
                        details.contentRatings
                            ?.firstOrNull { it.iso31661 == userCountryCode }
                            ?.rating
                    }
                    MediaType.MOVIE -> {
                        details.releaseDates
                            ?.firstOrNull { it.iso31661 == userCountryCode }
                            ?.certification
                    }
                    else -> null
                }
            }

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
                                Text(
                                    text = genre.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = onSurfaceDark.copy(
                                    alpha = .02f
                                ),
                                labelColor = surfaceVariantDark
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = onSurfaceDark.copy(alpha = .02f)
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
                                Text(
                                    text = releaseDate.year.toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = onSurfaceDark.copy(
                                    alpha = .02f
                                ),
                                disabledLabelColor = surfaceVariantDark
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = onSurfaceDark.copy(alpha = .02f)
                            ),
                            shape = CircleShape
                        )
                    }
                }
                contentRating?.let { contentRating ->
                    add {
                        SuggestionChip(
                            enabled = false,
                            onClick = {},
                            label = {
                                Text(
                                    text = contentRating,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = onSurfaceDark.copy(
                                    alpha = .02f
                                ),
                                disabledLabelColor = surfaceVariantDark
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = onSurfaceDark.copy(alpha = .02f)
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
                                Text(
                                    text = formatRuntime(runtime).asString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = onSurfaceDark.copy(
                                    alpha = .02f
                                ),
                                disabledLabelColor = surfaceVariantDark
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = onSurfaceDark.copy(alpha = .02f)
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
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = onSurfaceDark.copy(
                                    alpha = .02f
                                ),
                                disabledLabelColor = surfaceVariantDark
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = onSurfaceDark.copy(alpha = .02f)
                            ),
                            shape = CircleShape
                        )
                    }
                }
            }.takeIf { it.isNotEmpty() }?.let { components ->
                FlowRow(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
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