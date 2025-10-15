package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.presentation.components.AutoResizedText
import com.personal.tmdb.detail.presentation.detail.DetailState

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    detailState: DetailState
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = TMDB.logoW500(detailState.logo?.filePath),
        contentDescription = null,
        contentScale = ContentScale.Inside
    ) {
        val state by painter.state.collectAsStateWithLifecycle()
        when (state) {
            AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .width(240.dp)
                )
            }
            is AsyncImagePainter.State.Error -> {
                AutoResizedText(
                    modifier = Modifier.fillMaxWidth(),
                    text = detailState.details?.name ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.sizeIn(maxWidth = 240.dp, maxHeight = 64.dp)
                )
            }
        }
    }
}