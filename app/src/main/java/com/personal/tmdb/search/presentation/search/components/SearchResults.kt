package com.personal.tmdb.search.presentation.search.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.UiTextException
import com.personal.tmdb.core.domain.util.fadingEdges
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.core.presentation.components.MessageContainer
import com.personal.tmdb.search.presentation.search.SearchState
import com.personal.tmdb.search.presentation.search.SearchUiEvent
import kotlinx.coroutines.launch

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    searchState: () -> SearchState,
    preferencesState: () -> PreferencesState,
    searchUiEvent: (SearchUiEvent) -> Unit
) {
    val searchResults = searchState().searchResults?.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    LaunchedEffect(searchResults?.loadState?.refresh) {
        if (searchResults?.loadState?.refresh == LoadState.Loading) lazyGridState.animateScrollToItem(0)
    }

    Column(
        modifier = modifier
    ) {
        SearchFilterTabs(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            selectedTab = searchState().searchType,
            onSelect = {
                if (searchState().searchType == it) {
                    scope.launch { lazyGridState.animateScrollToItem(0) }
                } else {
                    searchUiEvent(SearchUiEvent.SetSearchType(it))
                }
            }
        )
        if (searchResults?.loadState?.refresh is LoadState.Error) {
            val error = (searchResults.loadState.refresh as LoadState.Error).error
            MessageContainer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                content = {
                    Text(
                        text = stringResource(id = R.string.error_general),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = when (error) {
                            is UiTextException -> error.uiText.asString()
                            else -> error.localizedMessage ?: stringResource(id = R.string.error_unknown)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        textAlign = TextAlign.Center
                    )
                },
                onRetry = {
                    searchResults.retry()
                }
            )
        } else {
            MediaGrid(
                modifier = Modifier.fadingEdges(lazyGridState, topEdgeHeight = 16.dp, bottomEdgeHeight = 0.dp),
                lazyGridState = lazyGridState,
                contentPadding = PaddingValues(16.dp),
                items = {
                    searchResults?.let { results ->
                        when (results.loadState.refresh) {
                            LoadState.Loading -> {
                                items(
                                    count = 20,
                                    contentType = { "Poster" }
                                ) {
                                    MediaPosterShimmer(
                                        modifier = Modifier.fillMaxWidth(),
                                        height = Dp.Unspecified,
                                        showTitle = preferencesState().showTitle
                                    )
                                }
                            }
                            else -> {
                                if (results.itemCount == 0) {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        MessageContainer(
                                            modifier = Modifier.fillMaxWidth(),
                                            content = {
                                                Text(
                                                    text = stringResource(id = R.string.empty_search),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    textAlign = TextAlign.Center
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.empty_search_suggest),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        )
                                    }
                                } else {
                                    items(
                                        count = results.itemCount,
                                        key = results.itemKey { it.uuid },
                                        contentType = results.itemContentType { "Poster" }
                                    ) { index ->
                                        results[index]?.let { mediaInfo ->
                                            MediaPoster(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .animateItem(),
                                                onNavigateTo = { searchUiEvent(SearchUiEvent.OnNavigateTo(it)) },
                                                height = Dp.Unspecified,
                                                mediaInfo = mediaInfo,
                                                mediaType = mediaInfo.mediaType ?: searchState().searchType,
                                                showTitle = preferencesState().showTitle,
                                                showVoteAverage = preferencesState().showVoteAverage
                                            )
                                        }
                                    }
                                    when (results.loadState.append) {
                                        is LoadState.Error -> {
                                            val error = (results.loadState.append as LoadState.Error).error
                                            item(
                                                span = { GridItemSpan(maxLineSpan) },
                                                contentType = "MessageContainer"
                                            ) {
                                                MessageContainer(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    content = {
                                                        Text(
                                                            text = when (error) {
                                                                is UiTextException -> error.uiText.asString()
                                                                else -> error.localizedMessage ?: stringResource(id = R.string.error_unknown)
                                                            },
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    },
                                                    onRetry = {
                                                        results.retry()
                                                    }
                                                )
                                            }
                                        }
                                        LoadState.Loading -> {
                                            items(
                                                count = 2,
                                                contentType = { "ShimmerPoster" }
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
                                        else -> Unit
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}