package com.personal.tmdb.detail.presentation.episodes.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.fadingEdges
import com.personal.tmdb.core.domain.util.formatEpisodesCount
import com.personal.tmdb.core.domain.util.formatVoteAverage
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.detail.presentation.episodes.EpisodesUiEvent
import com.personal.tmdb.detail.presentation.episodes.SeasonState
import com.personal.tmdb.ui.theme.onSurfaceLight
import com.personal.tmdb.ui.theme.surfaceLight

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.seasonSelect(
    seasonState: () -> SeasonState,
    episodesUiEvent: (EpisodesUiEvent) -> Unit
) {
    item {
        var showDialog by rememberSaveable { mutableStateOf(false) }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (seasonState().loading && seasonState().mediaDetail == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = "Season name",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Transparent
                    )
                }
            } else {
                seasonState().mediaDetail?.seasons?.takeIf { it.isNotEmpty() }?.let { seasons ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .then(
                                if (seasons.size > 1) {
                                    Modifier.clickable { showDialog = true }
                                } else {
                                    Modifier
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val season = seasons.find { it.seasonNumber == seasonState().seasonNumber }
                        Text(
                            modifier = Modifier.weight(1f),
                            text = season?.name ?: season?.seasonNumber?.toString() ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (seasons.size > 1) {
                            val rotation by animateIntAsState(
                                targetValue = if (showDialog) 180 else 0,
                                label = "Rotation animation"
                            )
                            Icon(
                                modifier = Modifier.rotate(rotation.toFloat()),
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        }
                    }
                    if (showDialog) {
                        Dialog(
                            onDismissRequest = { showDialog = false },
                            properties = DialogProperties(dismissOnClickOutside = false)
                        ) {
                            val lazyListState = rememberLazyListState()
                            (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(.8f)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fadingEdges(lazyListState),
                                        state = lazyListState
                                    ) {
                                        items(
                                            items = seasons,
                                            key = { it.id }
                                        ) { season ->
                                            CustomListItem(
                                                onClick = {
                                                    episodesUiEvent(
                                                        EpisodesUiEvent.SetSelectedSeason(
                                                            seasonState().mediaId,
                                                            season.seasonNumber
                                                        )
                                                    )
                                                    showDialog = false
                                                },
                                                headlineContent = {
                                                    Text(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        text = season.name ?: season.seasonNumber.toString(),
                                                        fontSize = if (season.seasonNumber == seasonState().seasonNumber) 22.sp else 18.sp,
                                                        fontWeight = if (season.seasonNumber == seasonState().seasonNumber) FontWeight.SemiBold else FontWeight.Normal,
                                                        color = if (season.seasonNumber == seasonState().seasonNumber) surfaceLight else surfaceLight.copy(alpha = .7f),
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                                IconButton(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(56.dp),
                                    onClick = { showDialog = false },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = surfaceLight, contentColor = onSurfaceLight)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Close"
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (seasonState().loading) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    text = "Episodes and rating",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Transparent
                )
            } else {
                buildList<@Composable FlowRowScope.() -> Unit> {
                    seasonState().seasonInfo?.episodes?.takeIf { it.isNotEmpty() }?.let { episodes ->
                        add {
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = formatEpisodesCount(episodes.size),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                    seasonState().seasonInfo?.voteAverage?.takeIf { it != 0f }?.let { voteAverage ->
                        add {
                            Row(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.surfaceVariant
                                )
                                Text(
                                    text = formatVoteAverage(voteAverage),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }.takeIf { it.isNotEmpty() }?.let { components ->
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        components.forEachIndexed { index, component ->
                            component()
                            if (index != components.lastIndex) {
                                Icon(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.icon_fiber_manual_record_fill1_wght400),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}