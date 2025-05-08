package com.personal.tmdb.search.presentation.search.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.search.presentation.search.SearchUiEvent

@Composable
fun SearchPopularPeople(
    modifier: Modifier = Modifier,
    popularPeople: LazyPagingItems<MediaInfo>,
    preferencesState: () -> PreferencesState,
    searchUiEvent: (SearchUiEvent) -> Unit
) {
    if (popularPeople.itemCount > 0) {
        MediaCarousel(
            modifier = modifier,
            titleContent = {
                Text(text = stringResource(id = R.string.popular_people))
            },
            items = {
                items(
                    count = popularPeople.itemCount,
                    key = popularPeople.itemKey { it.uuid },
                    contentType = popularPeople.itemContentType { "Poster" }
                ) { index ->
                    popularPeople[index]?.let { mediaInfo ->
                        MediaPoster(
                            onNavigateTo = { searchUiEvent(SearchUiEvent.OnNavigateTo(it)) },
                            mediaInfo = mediaInfo,
                            mediaType = MediaType.PERSON,
                            showTitle = preferencesState().showTitle,
                            showVoteAverage = preferencesState().showVoteAverage,
                        )
                    }
                }
                when (popularPeople.loadState.append) {
                    LoadState.Loading -> {
                        item {
                            MediaPosterShimmer(showTitle = preferencesState().showTitle)
                        }
                    }
                    else -> Unit
                }
            }
        )
    }
}

@Composable
fun SearchPopularPeopleShimmer(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState
) {
    MediaCarousel(
        modifier = modifier,
        titleContent = {
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect(),
                text = stringResource(id = R.string.popular_people),
                color = Color.Transparent
            )
        },
        items = {
            items(20) {
                MediaPosterShimmer(showTitle = preferencesState().showTitle)
            }
        },
        userScrollEnabled = false
    )
}