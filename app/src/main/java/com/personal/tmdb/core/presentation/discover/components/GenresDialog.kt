package com.personal.tmdb.core.presentation.discover.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.fadingEdges
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.detail.data.models.Genre
import com.personal.tmdb.core.presentation.discover.DiscoverState
import com.personal.tmdb.ui.theme.onSurfaceLight
import com.personal.tmdb.ui.theme.surfaceLight

@Composable
fun GenresDialog(
    discoverState: () -> DiscoverState,
    onDismissRequest: () -> Unit,
    selectGenre: (genre: Genre?) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
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
                discoverState().genresInfo?.genres?.let { genres ->
                    LazyColumn(
                        modifier = Modifier.fadingEdges(lazyListState),
                        state = lazyListState
                    ) {
                        item {
                            CustomListItem(
                                onClick = {
                                    selectGenre(null)
                                    onDismissRequest()
                                },
                                headlineContent = {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(id = R.string.all_genres),
                                        fontSize = if (discoverState().selectedGenre == null) 22.sp else 18.sp,
                                        fontWeight = if (discoverState().selectedGenre == null) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (discoverState().selectedGenre == null) surfaceLight else surfaceLight.copy(alpha = .7f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                        items(
                            items = genres,
                            key = { it.id }
                        ) { genre ->
                            CustomListItem(
                                onClick = {
                                    selectGenre(genre)
                                    onDismissRequest()
                                },
                                headlineContent = {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = genre.name,
                                        fontSize = if (genre == discoverState().selectedGenre) 22.sp else 18.sp,
                                        fontWeight = if (genre == discoverState().selectedGenre) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (genre == discoverState().selectedGenre) surfaceLight else surfaceLight.copy(alpha = .7f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    }
                }
            }
            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp),
                onClick = onDismissRequest,
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