package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.domain.util.toPxFloat
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.ui.theme.surfaceContainerDark
import com.personal.tmdb.ui.theme.surfaceContainerHighDark
import com.personal.tmdb.ui.theme.surfaceDark

@Composable
fun Backdrop(
    modifier: Modifier = Modifier,
    detailState: DetailState,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = detailState.details?.backdropPath == null && detailState.loadState is LoadState.Loading,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { loading ->
            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect(
                            primaryColor = surfaceContainerHighDark,
                            secondaryColor = surfaceContainerDark
                        )
                )
            } else {
                val painter = rememberAsyncImagePainter(TMDB.backdropW1280(detailState.details?.backdropPath))
                val painterState by painter.state.collectAsStateWithLifecycle()
                when (painterState) {
                    is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painter,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(surfaceContainerHighDark)
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(surfaceDark.copy(alpha = .5f), Color.Transparent),
                        endY = 80.dp.toPxFloat()
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, surfaceDark),
                        startY = 240.dp.toPxFloat()
                    )
                )
        )
    }
}