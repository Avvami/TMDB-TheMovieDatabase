package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.personal.tmdb.core.domain.util.compactDecimalFormat
import com.personal.tmdb.core.domain.util.formatVoteAverage
import com.personal.tmdb.core.domain.util.getColorForVoteAverage
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.presentation.detail.DetailState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Rating(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    sharedTransitionScope: SharedTransitionScope
) {
    with(sharedTransitionScope) {
        AnimatedContent(
            modifier = modifier,
            targetState = detailState.loadState is LoadState.Loading && detailState.details == null,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { loading ->
            if (loading) {
                Text(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("RATING"),
                            animatedVisibilityScope = this,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect(),
                    text = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.labelLarge.toSpanStyle()
                        ) {
                            append("00 (00K)  originalName")
                        }
                    },
                    textAlign = TextAlign.Center,
                    color = Color.Transparent
                )
            } else {
                detailState.details?.let { details ->
                    Text(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("RATING"),
                                animatedVisibilityScope = this,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            ),
                        text = buildAnnotatedString {
                            details.voteAverage?.let { voteAverage ->
                                withStyle(
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = getColorForVoteAverage(voteAverage)
                                    ).toSpanStyle()
                                ) {
                                    append(formatVoteAverage(voteAverage))
                                }
                            }
                            details.voteCount?.takeIf { it != 0 }?.let { voteCount ->
                                withStyle(
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ).toSpanStyle()
                                ) {
                                    append(" (${compactDecimalFormat(voteCount.toLong())})")
                                }
                            }
                            details.originalName.takeIf { it != details.name }?.let { originalName ->
                                withStyle(
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    ).toSpanStyle()
                                ) {
                                    append("  $originalName")
                                }
                            }
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}