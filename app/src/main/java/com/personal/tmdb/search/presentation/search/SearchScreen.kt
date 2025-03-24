package com.personal.tmdb.search.presentation.search

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.search.presentation.search.components.SearchField
import com.personal.tmdb.search.presentation.search.components.SearchResults
import com.personal.tmdb.search.presentation.search.components.SearchSuggestion
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

@Composable
fun SearchScreenRoot(
    bottomPadding: Dp,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    SearchScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        lazyGridState = lazyGridState,
        preferencesState = preferencesState,
        searchState = { searchState },
        searchUiEvent = { event ->
            when (event) {
                is SearchUiEvent.OnNavigateTo -> {
                    onNavigateTo(event.route)
                }
                else -> Unit
            }
            viewModel.searchUiEvent(event)
        }
    )
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    preferencesState: () -> PreferencesState,
    searchState: () -> SearchState,
    searchUiEvent: (SearchUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    BackHandler(enabled = !backPressHandled) {
        if (searchState().searchQuery.isNotBlank()) {
            focusManager.clearFocus()
            searchUiEvent(SearchUiEvent.OnSearchQueryChange(""))
            searchUiEvent(SearchUiEvent.SetSearchType(MediaType.MULTI))
        } else {
            /*TODO: Find a better solution???*/
            backPressHandled = true
            scope.launch {
                awaitFrame()
                onBackPressedDispatcher?.onBackPressed()
                backPressHandled = false
            }
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
        ) {
            SearchField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                searchQuery = searchState()::searchQuery,
                searchUiEvent = searchUiEvent
            )
            AnimatedContent(
                targetState = searchState().searchResults == null && !searchState().searching && searchState().searchErrorMessage == null,
                label = "Search screen content animation"
            ) { targetState ->
                if (targetState) {
                    SearchSuggestion(
                        searchState = searchState,
                        preferencesState = preferencesState,
                        searchUiEvent = searchUiEvent
                    )
                } else {
                    SearchResults(
                        lazyGridState = lazyGridState,
                        searchState = searchState,
                        preferencesState = preferencesState,
                        searchUiEvent = searchUiEvent
                    )
                }
            }
        }
    }
}