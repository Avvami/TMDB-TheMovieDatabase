package com.personal.tmdb.core.domain.util

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

@Composable
fun Modifier.shimmerEffect(
    primaryColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    secondaryColor: Color = MaterialTheme.colorScheme.surfaceContainer
): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "Transition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(tween(2000)), label = "Transition"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(secondaryColor, primaryColor, secondaryColor),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

fun Modifier.negativeHorizontalPadding(
    padding: Dp
): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints.offset(horizontal = -padding.roundToPx() * 2))
        layout(
            width = placeable.width + padding.roundToPx() * 2,
            height = placeable.height
        ) {
            placeable.place(+padding.roundToPx(), 0)
        }
    }
)

fun Modifier.fadingEdges(
    state: ScrollableState,
    topEdgeHeight: Dp = 24.dp,
    bottomEdgeHeight: Dp = 24.dp
) = composed {
    val animatedTopEdgeHeight by animateDpAsState(
        targetValue = if (state.canScrollBackward) topEdgeHeight else 0.dp,
        label = "TopFadeDpAnimation"
    )
    val animatedBottomEdgeHeight by animateDpAsState(
        targetValue = if (state.canScrollForward) bottomEdgeHeight else 0.dp,
        label = "BottomFadeDpAnimation"
    )
    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val topEdgeHeightPx = animatedTopEdgeHeight.toPx()
            if (topEdgeHeight.toPx() < size.height && topEdgeHeightPx > 1f) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = topEdgeHeightPx,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }

            val bottomEdgeHeightPx = animatedBottomEdgeHeight.toPx()
            if (bottomEdgeHeight.toPx() < size.height && bottomEdgeHeightPx > 1f) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = size.height - bottomEdgeHeightPx,
                        endY = size.height,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }
        }
}