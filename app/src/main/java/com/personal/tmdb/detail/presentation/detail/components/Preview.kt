package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.personal.tmdb.UserState
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.surfaceDark

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Preview(
    modifier: Modifier = Modifier,
    userState: () -> UserState,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    val dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
    SharedTransitionLayout {
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
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Logo(
                    detailState = detailState,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
                Rating(
                    detailState = detailState,
                    sharedTransitionScope = this@SharedTransitionLayout
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