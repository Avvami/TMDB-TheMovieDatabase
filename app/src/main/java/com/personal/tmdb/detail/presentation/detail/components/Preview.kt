package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.surfaceDark

@Composable
fun Preview(
    modifier: Modifier = Modifier,
    userState: () -> UserState,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    val dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
    Column(
        modifier = modifier
            .background(surfaceDark)
            .drawBehind {
                val strokeWidthPx = 1.dp.toPx()
                drawLine(
                    color = dividerColor,
                    start = Offset(x = 0f, y = size.height),
                    end = Offset(x = size.width, y = size.height),
                    strokeWidth = strokeWidthPx
                )
            }
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Backdrop(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            detailState = detailState
        )
        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = detailState.loadState is LoadState.Loading,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentAlignment = Alignment.Center
        ) { loading ->
            if (loading) {
                PreviewShimmer(
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Logo(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        detailState = detailState
                    )
                    Rating(
                        detailState = detailState
                    )
                    AttributeChips(
                        userState = userState,
                        detailState = detailState,
                        detailUiEvent = detailUiEvent,
                    )
                    ActionButtons(
                        userState = userState,
                        detailState = detailState,
                        detailUiEvent = detailUiEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviewShimmer(
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(64.dp)
                    .width(240.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmerEffect()
            )
            Text(
                modifier = Modifier
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
                            Text(
                                text = "Meta",
                                style = MaterialTheme.typography.bodyMedium
                            )
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
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = .05f
                                    )
                                ),
                                shape = CircleShape
                            )
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}