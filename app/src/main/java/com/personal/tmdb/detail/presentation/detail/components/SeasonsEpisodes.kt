package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.clickableNoIndication
import com.personal.tmdb.core.domain.util.formatDate
import com.personal.tmdb.core.domain.util.formatRuntime
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.components.ProvideContentColorTextStyle
import com.personal.tmdb.detail.data.models.Season
import com.personal.tmdb.detail.domain.models.EpisodeToAir
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@Composable
fun SeasonsEpisodes(
    modifier: Modifier = Modifier,
    details: MediaDetail,
    seasons: List<Season>,
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
                            Route.Episodes(
                                mediaId = details.id,
                                seasonNumber = details.lastEpisodeToAir?.seasonNumber
                                    ?: seasons.firstOrNull { it.seasonNumber == 1 }?.seasonNumber
                                    ?: seasons[seasons.lastIndex].seasonNumber
                            )
                        )
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.seasons_episodes),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(R.drawable.icon_keyboard_arrow_right_fill0_wght400),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        details.lastEpisodeToAir?.let { lastEpisodeToAir ->
            EpisodeToAir(
                onNavigateTo = { detailUiEvent(DetailUiEvent.OnNavigateTo(it)) },
                episodeToAir = lastEpisodeToAir,
                supportingText = UiText.StringResource(R.string.last_episode)
            )
        }
        details.nextEpisodeToAir?.let { nextEpisodeToAir ->
            EpisodeToAir(
                onNavigateTo = { detailUiEvent(DetailUiEvent.OnNavigateTo(it)) },
                episodeToAir = nextEpisodeToAir,
                supportingText = UiText.StringResource(R.string.next_episode)
            )
        }
    }
}

@Composable
fun EpisodeToAir(
    modifier: Modifier = Modifier,
    onNavigateTo: (route: Route) -> Unit,
    episodeToAir: EpisodeToAir,
    supportingText: UiText
) {
    Row(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = MaterialTheme.shapes.medium.topStart,
                    topEnd = MaterialTheme.shapes.extraSmall.topEnd,
                    bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd,
                    bottomStart = MaterialTheme.shapes.medium.bottomStart
                )
            )
            .height(IntrinsicSize.Min)
            .clickable {
                onNavigateTo(
                    Route.Episode(
                        mediaId = episodeToAir.showId,
                        seasonNumber = episodeToAir.seasonNumber,
                        episodeNumber = episodeToAir.episodeNumber
                    )
                )
            },
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .aspectRatio(16 / 9f)
                .clip(MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = TMDB.imageOriginal(episodeToAir.stillPath),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                episodeToAir.name?.let { name ->
                    Text(
                        text = "${
                            stringResource(
                                id = R.string.season_episode,
                                episodeToAir.seasonNumber,
                                episodeToAir.episodeNumber
                            )
                        } $name",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                buildList<@Composable FlowRowScope.() -> Unit> {
                    episodeToAir.airDate?.let { airDate ->
                        add { Text(text = formatDate(airDate)) }
                    }
                    episodeToAir.runtime?.let { runtime ->
                        add { Text(text = formatRuntime(runtime).asString()) }
                    }
                }.takeIf { it.isNotEmpty() }?.let { components ->
                    ProvideContentColorTextStyle(
                        contentColor = MaterialTheme.colorScheme.surfaceVariant,
                        textStyle = MaterialTheme.typography.bodySmall
                    ) {
                        FlowRow(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            itemVerticalAlignment = Alignment.CenterVertically
                        ) {
                            components.forEachIndexed { index, component ->
                                component()
                                if (index != components.lastIndex) {
                                    Icon(
                                        modifier = Modifier.size(6.dp),
                                        painter = painterResource(id = R.drawable.icon_fiber_manual_record_fill1_wght400),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Text(
                modifier = Modifier.align(Alignment.End),
                text = supportingText.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}