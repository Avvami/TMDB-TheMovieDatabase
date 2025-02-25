package com.personal.tmdb.profile.presentation.lists.presentation.lists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.components.ListItem
import com.personal.tmdb.core.presentation.components.ListItemShimmer
import com.personal.tmdb.core.presentation.components.MediaGrid

@Composable
fun ListsScreenRoot(
    bottomPadding: Dp,
    canNavigateBack: Boolean = true,
    lazyGridState: LazyGridState,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    viewModel: ListsViewModel = hiltViewModel()
) {
    val listsState by viewModel.listsState.collectAsStateWithLifecycle()
    ListsScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        canNavigateBack = canNavigateBack,
        lazyGridState = lazyGridState,
        listsState = { listsState },
        listsUiEvent = { event ->
            when (event) {
                ListsUiEvent.OnNavigateBack -> onNavigateBack()
                is ListsUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                else -> Unit
            }
            viewModel.listsUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListsScreen(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
    lazyGridState: LazyGridState,
    listsState: () -> ListsState,
    listsUiEvent: (ListsUiEvent) -> Unit
) {
    LaunchedEffect(key1 = true) {
        if (!lazyGridState.canScrollBackward) {
            listsUiEvent(ListsUiEvent.GetLists(1))
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.my_lists),
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(
                            onClick = { listsUiEvent(ListsUiEvent.OnNavigateBack) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        MediaGrid(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
            columns = GridCells.Adaptive(360.dp),
            items = {
                if (listsState().loading && listsState().lists == null) {
                    items(
                        count = 4,
                        contentType = { "List" }
                    ) {
                        ListItemShimmer(
                            modifier = Modifier.fillMaxWidth(),
                            height = Dp.Unspecified
                        )
                    }
                } else {
                    listsState().errorMessage?.let {  }
                    listsState().lists?.results?.let { lists ->
                        if (lists.isEmpty()) {
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Create a new list",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(
                                items = lists,
                                key = { it.id }
                            ) { listInfo ->
                                ListItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem(),
                                    onNavigateTo = { listsUiEvent(ListsUiEvent.OnNavigateTo(it)) },
                                    listInfo = listInfo,
                                    height = Dp.Unspecified
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}