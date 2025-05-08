package com.personal.tmdb.search.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.personal.tmdb.R
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MessageContainer
import com.personal.tmdb.search.presentation.search.SearchState
import com.personal.tmdb.search.presentation.search.SearchUiEvent

@Composable
fun SearchSuggestion(
    modifier: Modifier = Modifier,
    searchState: () -> SearchState,
    preferencesState: () -> PreferencesState,
    searchUiEvent: (SearchUiEvent) -> Unit
) {
    val popularPeople = searchState().popularPeople?.collectAsLazyPagingItems()
    if (searchState().loading || popularPeople?.loadState?.refresh is LoadState.Loading) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            item {
                SearchTrendingShimmer()
            }
            item {
                SearchPopularPeopleShimmer(
                    preferencesState = preferencesState
                )
            }
        }
    } else {
        if (searchState().errorMessage != null) {
            searchState().errorMessage?.let { message ->
                MessageContainer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    content = {
                        Text(
                            text = stringResource(id = R.string.error_general),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = message.asString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    },
                    onRetry = {
                        searchUiEvent(SearchUiEvent.RetrySuggestionsRequest)
                    }
                )
            }
        } else {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                searchState().trending?.let {
                    item {
                        SearchTrending(
                            trending = it,
                            searchUiEvent = searchUiEvent
                        )
                    }
                }
                popularPeople?.let {
                    item {
                        SearchPopularPeople(
                            popularPeople = it,
                            preferencesState = preferencesState,
                            searchUiEvent = searchUiEvent
                        )
                    }
                }
            }
        }
    }
}