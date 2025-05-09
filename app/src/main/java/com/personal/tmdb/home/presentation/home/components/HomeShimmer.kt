package com.personal.tmdb.home.presentation.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer

@Composable
fun HomeShimmer(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        item {
            Box(
                modifier = Modifier
                    .height(450.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                        shape = MaterialTheme.shapes.extraLarge
                    )
            )
        }
        item {
            MediaCarousel(
                titleContent = {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = stringResource(id = R.string.trending_today),
                        color = Color.Transparent
                    )
                },
                items = {
                    items(count = 20) {
                        MediaPosterShimmer(showTitle = preferencesState().showTitle)
                    }
                },
                userScrollEnabled = false
            )
        }
        item {
            MediaCarousel(
                titleContent = {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = stringResource(id = R.string.popular_movies),
                        color = Color.Transparent
                    )
                },
                items = {
                    items(count = 20) {
                        MediaPosterShimmer(showTitle = preferencesState().showTitle)
                    }
                },
                userScrollEnabled = false
            )
        }
        item {
            MediaCarousel(
                titleContent = {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = stringResource(id = R.string.popular_tv),
                        color = Color.Transparent
                    )
                },
                items = {
                    items(count = 20) {
                        MediaPosterShimmer(showTitle = preferencesState().showTitle)
                    }
                },
                userScrollEnabled = false
            )
        }
        item {
            MediaCarousel(
                titleContent = {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = stringResource(id = R.string.upcoming_movies),
                        color = Color.Transparent
                    )
                },
                items = {
                    items(count = 20) {
                        MediaPosterShimmer(showTitle = preferencesState().showTitle)
                    }
                },
                userScrollEnabled = false
            )
        }
    }
}