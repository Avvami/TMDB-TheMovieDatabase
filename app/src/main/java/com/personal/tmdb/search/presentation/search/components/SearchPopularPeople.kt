package com.personal.tmdb.search.presentation.search.components

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.search.presentation.search.SearchUiEvent

@Composable
fun SearchPopularPeople(
    modifier: Modifier = Modifier,
    popularPeople: () -> MediaResponseInfo?,
    preferencesState: () -> PreferencesState,
    searchUiEvent: (SearchUiEvent) -> Unit
) {
    MediaCarousel(
        modifier = modifier,
        titleContent = {
            Text(text = stringResource(id = R.string.popular_people))
        },
        items = {
            if (popularPeople() == null) {
                items(20) {
                    MediaPosterShimmer(showTitle = preferencesState().showTitle)
                }
            } else {
                popularPeople()?.results?.let { people ->
                    items(
                        items = people,
                        key = { it.id }
                    ) { mediaInfo ->
                        MediaPoster(
                            onNavigateTo = { searchUiEvent(SearchUiEvent.OnNavigateTo(it)) },
                            mediaInfo = mediaInfo,
                            mediaType = MediaType.PERSON,
                            showTitle = preferencesState().showTitle,
                            showVoteAverage = preferencesState().showVoteAverage,
                        )
                    }
                }
            }
        }
    )
}