package com.personal.tmdb.detail.presentation.detail.components

import android.os.Build
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberPlatformOverscrollFactory
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.clickableNoIndication
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.domain.models.Cast
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@Composable
fun CastCrew(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    castList: List<Cast>,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickableNoIndication {
                    detailUiEvent(
                        DetailUiEvent.OnNavigateTo(
                            Route.Cast(
                                mediaName = detailState.details?.name ?: "",
                                mediaType = detailState.mediaType.name.lowercase(),
                                mediaId = detailState.mediaId
                            )
                        )
                    )
                }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.cast_crew),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(R.drawable.icon_keyboard_arrow_right_fill0_wght400),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        if (castList.size < 3) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                castList.take(3).fastForEach { cast ->
                    CastMember(
                        modifier = Modifier.fillMaxWidth(),
                        cast = cast,
                        onClick = {
                            detailUiEvent(
                                DetailUiEvent.OnNavigateTo(
                                    Route.Person(
                                        personName = cast.name ?: "",
                                        personId = cast.id
                                    )
                                )
                            )
                        }
                    )
                }
            }
        } else {
            CompositionLocalProvider(
                LocalOverscrollFactory provides if (Build.VERSION.SDK_INT > 30) {
                    rememberPlatformOverscrollFactory()
                } else null
            ) {
                LazyHorizontalGrid(
                    modifier = Modifier.heightIn(max = 208.dp),
                    rows = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    items(
                        items = castList.take(15),
                        key = { it.id }
                    ) { cast ->
                        CastMember(
                            modifier = Modifier.width(280.dp),
                            cast = cast,
                            onClick = {
                                detailUiEvent(
                                    DetailUiEvent.OnNavigateTo(
                                        Route.Person(
                                            personName = cast.name ?: "",
                                            personId = cast.id
                                        )
                                    )
                                )
                            }
                        )
                    }
                    if (castList.size > 15) {
                        item(
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.medium)
                                        .clickableNoIndication {
                                            detailUiEvent(
                                                DetailUiEvent.OnNavigateTo(
                                                    Route.Cast(
                                                        mediaName = detailState.details?.name ?: "",
                                                        mediaType = detailState.mediaType.name.lowercase(),
                                                        mediaId = detailState.mediaId
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
                }
            }
        }
    }
}

@Composable
private fun CastMember(
    modifier: Modifier = Modifier,
    cast: Cast,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickableNoIndication {
                onClick()
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = TMDB.profileW185(cast.profilePath),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Column {
            Text(
                text = cast.name ?: stringResource(R.string.no_name),
                style = MaterialTheme.typography.titleMedium
            )
            cast.character?.let { character ->
                Text(
                    text = character,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}