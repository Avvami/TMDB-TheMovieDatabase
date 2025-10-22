package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.clickableNoIndication
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.detail.domain.models.Images
import com.personal.tmdb.detail.domain.util.ImageType
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@Composable
fun BackdropsPosters(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    images: Images,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    MediaCarousel(
        modifier = modifier,
        titleContent = {
            Row(
                modifier = Modifier
                    .clickableNoIndication(
                        enabled = (images.backdrops?.size ?: 0) > 10
                                || (images.posters?.size ?: 0) > 10
                    ) {
                        detailUiEvent(
                            DetailUiEvent.OnNavigateTo(
                                Route.Image(
                                    imageType = images.backdrops?.let { ImageType.BACKDROPS.name.lowercase() }
                                        ?: images.posters?.let { ImageType.POSTERS.name.lowercase() }
                                        ?: ImageType.UNKNOWN.name.lowercase(),
                                    imagesPath = C.MEDIA_IMAGES.format(
                                        detailState.mediaType.name.lowercase(),
                                        detailState.mediaId
                                    )
                                )
                            )
                        )
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.backdrops_posters)
                )
                if ((images.backdrops?.size ?: 0) > 10
                    || (images.posters?.size ?: 0) > 10) {
                    Icon(
                        painter = painterResource(R.drawable.icon_keyboard_arrow_right_fill0_wght400),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        },
        items = {
            images.backdrops?.let { backdrops ->
                items(
                    items = backdrops.take(10)
                ) { backdrop ->
                    AsyncImage(
                        modifier = Modifier
                            .height(200.dp)
                            .aspectRatio(16 / 9f)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { /*TODO*/ },
                        model = TMDB.backdropW1280(backdrop.filePath),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            images.posters?.let { posters ->
                items(
                    items = posters.take(10)
                ) { poster ->
                    AsyncImage(
                        modifier = Modifier
                            .height(200.dp)
                            .aspectRatio(2 / 3f)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable { /*TODO*/ },
                        model = TMDB.posterW300(poster.filePath),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            if ((images.backdrops?.size ?: 0) > 10 || (images.posters?.size ?: 0) > 10) {
                item {
                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickableNoIndication {
                                detailUiEvent(
                                    DetailUiEvent.OnNavigateTo(
                                        Route.Image(
                                            imageType = images.backdrops?.let { ImageType.BACKDROPS.name.lowercase() }
                                                ?: images.posters?.let { ImageType.POSTERS.name.lowercase() }
                                                ?: ImageType.UNKNOWN.name.lowercase(),
                                            imagesPath = C.MEDIA_IMAGES.format(
                                                detailState.mediaType.name.lowercase(),
                                                detailState.mediaId
                                            )
                                        )
                                    )
                                )
                            }
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.rotate(180f),
                                painter = painterResource(R.drawable.icon_arrow_left_alt_fill0_wght400),
                                contentDescription = null
                            )
                        }
                        Text(
                            text = stringResource(R.string.show_more),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    )
}