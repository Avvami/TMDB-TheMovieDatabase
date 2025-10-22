package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@Composable
fun ContentPage(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Preview(
                modifier = Modifier.onVisibilityChanged(minFractionVisible = .25f) { visible ->
                    detailUiEvent(DetailUiEvent.DimTopAppBar(!visible))
                },
                userState = userState,
                detailState = detailState,
                lazyListState = lazyListState,
                detailUiEvent = detailUiEvent
            )
        }
        if (detailState.details?.inProduction == true) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.another_season_coming),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        item {
            Overview(
                modifier = Modifier.padding(horizontal = 16.dp),
                detailState = detailState,
                detailUiEvent = detailUiEvent
            )
        }
        detailState.details?.let { details ->
            details.videos?.let { videos ->
                item {
                    TrailersTeasers(
                        videos = videos,
                        detailUiEvent = detailUiEvent
                    )
                }
            }
            details.belongsToCollection?.let { collection ->
                item {
                    Collection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        collection = collection,
                        detailUiEvent = detailUiEvent
                    )
                }
            }
            details.seasons?.let { seasons ->
                item {
                    SeasonsEpisodes(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        details = details,
                        seasons = seasons,
                        detailUiEvent = detailUiEvent
                    )
                }
            }
            detailState.selectedCountry?.let { (countryCode, _) ->
                details.watchProviders?.get(countryCode)?.let { available ->
                    item {
                        WatchNow(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            available = available,
                            detailUiEvent = detailUiEvent
                        )
                    }
                }
            }
            details.voteAverage?.let {
                item {
                    RatingReviews(
                        detailState = detailState,
                        userState = userState,
                        detailUiEvent = detailUiEvent
                    )
                }
            }
            details.cast?.let { castList ->
                item {
                    CastCrew(
                        detailState = detailState,
                        castList = castList,
                        detailUiEvent = detailUiEvent
                    )
                }
            }
            details.images?.let { images ->
                if (images.backdrops != null || images.posters != null) {
                    item {
                        BackdropsPosters(
                            detailState = detailState,
                            images = images,
                            detailUiEvent = detailUiEvent
                        )
                    }
                }
            }
            details.recommendations?.let { recommendations ->
                item {
                    MediaCarousel(
                        titleContent = {
                            Text(text = stringResource(id = R.string.recommendations))
                        },
                        items = {
                            items(
                                items = recommendations,
                                key = { it.id }
                            ) { media ->
                                MediaPoster(
                                    onNavigateTo = { detailUiEvent(DetailUiEvent.OnNavigateTo(it)) },
                                    mediaInfo = media,
                                    mediaType = media.mediaType ?: detailState.mediaType,
                                    showTitle = preferencesState().showTitle,
                                    showVoteAverage = preferencesState().showVoteAverage,
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}