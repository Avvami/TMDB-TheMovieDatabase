package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.formatDate
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.detail.domain.models.Video
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.onSurfaceDark

@Composable
fun TrailersTeasers(
    modifier: Modifier = Modifier,
    videos: List<Video>,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    MediaCarousel(
        modifier = modifier,
        titleContent = {
            Text(text = stringResource(id = R.string.trailers_teasers))
        },
        items = {
            items(
                items = videos,
                key = { it.id }
            ) { video ->
                val interactionSource = remember { MutableInteractionSource() }
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            detailUiEvent(
                                DetailUiEvent.OpenYTVideo(
                                    url = C.YT_VIDEO_BASE_URL.format(video.key)
                                )
                            )
                        },
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .aspectRatio(16 / 9f)
                            .clip(MaterialTheme.shapes.medium)
                            .indication(
                                interactionSource = interactionSource,
                                indication = LocalIndication.current
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier.matchParentSize(),
                            model = C.YT_THUMB_URL.format(video.key),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(R.drawable.icon_play_arrow_fill1_wght400),
                            contentDescription = null,
                            tint = onSurfaceDark
                        )
                    }
                    Column {
                        Text(
                            text = video.name ?: stringResource(R.string.no_name),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        video.publishedAt?.let { publishedAt ->
                            Text(
                                text = formatDate(publishedAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }
    )
}