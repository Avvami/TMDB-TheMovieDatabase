package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import androidx.compose.ui.util.fastForEach
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.domain.models.MediaDetailInfo
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailCredits(
    modifier: Modifier = Modifier,
    info: () -> MediaDetailInfo,
    mediaType: () -> MediaType,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.surfaceVariant,
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            info().genres?.takeIf { it.isNotEmpty() }?.let { genres ->
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(id = R.string.genres_list),
                        style = MaterialTheme.typography.labelLarge
                    )
                    genres.fastForEach { genre ->
                        SuggestionChip(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            onClick = { /*TODO: Navigate to genre screen*/ },
                            label = {
                                Text(text = genre.name)
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                labelColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = null
                        )
                    }
                }
            }
            info().createdBy?.takeIf { it.isNotEmpty() }?.let { createdBy ->
                AnnotatedListTextWithLinks(
                    annotationTag = AnnotationTag.CAST,
                    titlePrefix = stringResource(id = R.string.creator),
                    items = createdBy.map { AnnotatedItem(id = it.id, name = it.name) },
                    onNavigateTo = {
                        detailUiEvent(DetailUiEvent.OnNavigateTo(it))
                    }
                )
            }
            info().credits?.crew?.find { it.department == "Directing" }?.let { director ->
                AnnotatedListTextWithLinks(
                    annotationTag = AnnotationTag.CAST,
                    titlePrefix = stringResource(id = R.string.director),
                    items = listOf(
                        AnnotatedItem(id = director.id, name = director.name)
                    ),
                    onNavigateTo = {
                        detailUiEvent(DetailUiEvent.OnNavigateTo(it))
                    }
                )
            }
            info().cast?.takeIf { it.isNotEmpty() }?.let { cast ->
                AnnotatedOverflowListText(
                    modifier = Modifier
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            detailUiEvent(
                                DetailUiEvent.OnNavigateTo(
                                    Route.Cast(
                                        mediaName = info().name ?: "",
                                        mediaType = mediaType().name.lowercase(),
                                        mediaId = info().id
                                    )
                                )
                            )
                        },
                    titlePrefix = stringResource(id = R.string.starring),
                    items = cast.map { AnnotatedItem(id = it.id, name = it.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailCreditsShimmer() {
    val genres = listOf("Animation", "Drama", "Mystery", "Sci-Fi & Fantasy")
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    text = stringResource(id = R.string.genres_list),
                    style = MaterialTheme.typography.labelLarge
                )
                genres.fastForEach { genre ->
                    SuggestionChip(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clip(MaterialTheme.shapes.small)
                            .shimmerEffect(),
                        enabled = false,
                        onClick = {},
                        label = {
                            Text(text = genre)
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            disabledContainerColor = Color.Transparent,
                            disabledLabelColor = Color.Transparent
                        ),
                        border = null
                    )
                }
            }
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
                text = "Creator: Elon Mask -_o",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
                text = "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}