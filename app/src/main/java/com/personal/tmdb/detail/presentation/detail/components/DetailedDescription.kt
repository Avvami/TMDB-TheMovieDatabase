package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@Composable
fun DetailedDescription(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    userState: () -> UserState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    detailState.details?.let { details ->
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
        ) {
            details.tagline?.let { tagline ->
                item {
                    Text(
                        text = tagline,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            item {
                Text(
                    text = details.overview
                        ?: stringResource(R.string.no_overview).format(details.name ?: ""),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            details.genres?.let { genres ->
                item {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                    ) {
                        FlowRow(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            genres.fastForEach { genre ->
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
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        labelColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                                    ),
                                    shape = CircleShape
                                )
                            }
                        }
                    }
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    details.originalName?.let { originalName ->
                        AnnotatedListText(
                            prefix = stringResource(R.string.original_title),
                            items = listOf(originalName)
                        )
                    }
                    details.originalLanguage?.let { originalLanguage ->
                        AnnotatedListText(
                            prefix = stringResource(R.string.original_language),
                            items = listOf(originalLanguage)
                        )
                    }
                    details.status?.let { status ->
                        AnnotatedListText(
                            prefix = stringResource(R.string.status),
                            items = listOf(status)
                        )
                    }
                    details.networks?.let { networks ->
                        AnnotatedListText(
                            prefix = stringResource(if (networks.size == 1) R.string.network else R.string.networks),
                            items = networks.map { it.name }
                        )
                    }
                    details.productionCompanies?.let { productionCompanies ->
                        AnnotatedListText(
                            prefix = stringResource(if (productionCompanies.size == 1) R.string.production_company else R.string.production_companies),
                            items = productionCompanies.map { it.name }
                        )
                    }
                    details.budget?.let { budget ->
                        AnnotatedListText(
                            prefix = stringResource(R.string.budget),
                            items = listOf(stringResource(R.string.dollars, budget))
                        )
                    }
                    details.revenue?.let { revenue ->
                        AnnotatedListText(
                            prefix = stringResource(R.string.revenue),
                            items = listOf(stringResource(R.string.dollars, revenue))
                        )
                    }
                }
            }
            item {
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
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        contentRating?.let { contentRating ->
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
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                                ),
                                shape = CircleShape
                            )
                        }
                        details.originCountry?.let { originCountry ->
                            SuggestionChip(
                                enabled = false,
                                onClick = {},
                                label = {
                                    Text(
                                        text = originCountry.take(3).joinToString(", ") { it },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    disabledLabelColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                                ),
                                shape = CircleShape
                            )
                        }
                    }
                }
            }
        }
    }
}