package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.clickableWithOpaque
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.detail.presentation.detail.DetailUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Overview(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    SharedTransitionLayout {
        AnimatedContent(
            modifier = modifier,
            targetState = detailState.loadState is LoadState.Loading && detailState.details == null,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { loading ->
            if (loading) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("OVERVIEW"),
                                animatedVisibilityScope = this@AnimatedContent,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            )
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .shimmerEffect(),
                        text = "",
                        style = MaterialTheme.typography.bodyLarge,
                        minLines = 3
                    )
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .shimmerEffect(),
                        text = stringResource(id = R.string.more_details),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Transparent
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .clickableWithOpaque(
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            detailUiEvent(DetailUiEvent.SetUiState(DetailUiState.MORE_DETAILS))
                        },
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("OVERVIEW"),
                                animatedVisibilityScope = this@AnimatedContent,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            )
                            .skipToLookaheadSize(),
                        text = detailState.details?.overview
                            ?: stringResource(R.string.no_overview, detailState.details?.name ?: ""),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(id = R.string.more_details),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}