package com.personal.tmdb.profile.presentation.lists.presentation.list_details.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer

@Composable
fun ListDetailsScreenShimmer(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState
) {
    CompositionLocalProvider(
        LocalContentColor provides Color.Transparent
    ) {
        MediaGrid(
            modifier = modifier,
            contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
            span = {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = "ELON'S list",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = ""
                    )
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    ListCreatorShimmer(modifier = Modifier.padding(top = 8.dp))
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    ListMetadataShimmer(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        ) {
            items(
                count = 15
            ) {
                MediaPosterShimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    height = Dp.Unspecified,
                    showTitle = preferencesState().showTitle,
                )
            }
        }
    }
}