package com.personal.tmdb.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R

internal object DragHandleTokens {
    val DockedDragHandleHeight = 4.0.dp
    val DockedDragHandleWidth = 32.0.dp
    val DragHandleVerticalPadding = 16.dp
    val DragHandleHorizontalPadding = 16.dp
}

/** The optional visual marker placed on top of a bottom sheet to indicate it may be dragged. */
@Composable
fun CustomDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = DragHandleTokens.DockedDragHandleWidth,
    height: Dp = DragHandleTokens.DockedDragHandleHeight,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = DragHandleTokens.DragHandleHorizontalPadding,
        vertical = DragHandleTokens.DragHandleVerticalPadding
    )
) {
    val dragHandleDescription = "BottomSheetDragHandle"
    Surface(
        modifier = modifier
            .padding(contentPadding)
            .semantics { contentDescription = dragHandleDescription },
        color = color.copy(alpha = .4f),
        shape = shape
    ) {
        Box(modifier = Modifier.size(width = width, height = height))
    }
}

/** The optional visual marker and a button placed on top of a bottom sheet to indicate it may be dragged or closed. */
@Composable
fun CustomDragHandleWithButton(
    modifier: Modifier = Modifier,
    width: Dp = DragHandleTokens.DockedDragHandleWidth,
    height: Dp = DragHandleTokens.DockedDragHandleHeight,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = DragHandleTokens.DragHandleHorizontalPadding,
        vertical = DragHandleTokens.DragHandleVerticalPadding
    ),
    onClick: () -> Unit
) {
    val dragHandleDescription = "BottomSheetDragHandle"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        Surface(
            modifier = modifier
                .semantics { contentDescription = dragHandleDescription }
                .align(Alignment.TopCenter),
            color = color.copy(alpha = .4f),
            shape = shape
        ) {
            Box(
                modifier = Modifier.size(width = width, height = height)
            )
        }
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
        ) {
            FilledIconButton(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.BottomEnd),
                onClick = onClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                    contentColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.icon_close_fill0_wght400),
                    contentDescription = null
                )
            }
        }
    }
}