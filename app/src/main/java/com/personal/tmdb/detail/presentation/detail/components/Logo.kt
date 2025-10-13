package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.core.presentation.components.AutoResizedText
import com.personal.tmdb.detail.presentation.detail.DetailState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Logo(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    sharedTransitionScope: SharedTransitionScope
) {
    with(sharedTransitionScope) {
        AnimatedContent(
            modifier = modifier,
            targetState = detailState.logo == null && detailState.loadState is LoadState.Loading
        ) { loading ->
            if (loading) {
                Box(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("LOGO"),
                            animatedVisibilityScope = this,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                        )
                        .height(64.dp)
                        .width(240.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect()
                )
            } else {
                val painter = rememberAsyncImagePainter(TMDB.logoW500(detailState.logo?.filePath))
                val painterState by painter.state.collectAsStateWithLifecycle()
                when (painterState) {
                    AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("LOGO"),
                                    animatedVisibilityScope = this,
                                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                )
                                .height(64.dp)
                                .width(240.dp)
                        )
                    }
                    is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("LOGO"),
                                    animatedVisibilityScope = this,
                                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                )
                                .sizeIn(maxWidth = 240.dp, maxHeight = 64.dp),
                            painter = painter,
                            contentScale = ContentScale.Inside,
                            contentDescription = null
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        AutoResizedText(
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("LOGO"),
                                    animatedVisibilityScope = this,
                                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                )
                                .fillMaxWidth(),
                            text = detailState.details?.name ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}