package com.personal.tmdb.profile.presentation.lists.presentation.lists

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
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
import com.personal.tmdb.profile.presentation.lists.presentation.lists.components.CreateList

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
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = true) {
        if (!lazyGridState.canScrollBackward) {
            listsUiEvent(ListsUiEvent.GetLists(1))
        }
    }
    BackHandler {
        if (listsState().createEnabled) {
            /*TODO: Show warning dialog*/
            listsUiEvent(ListsUiEvent.CreateMode(false))
        } else {
            listsUiEvent(ListsUiEvent.OnNavigateBack)
        }
    }
    AnimatedContent(
        targetState = listsState().createEnabled,
        label = "Create list animation",
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { creating ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (creating) {
                            Text(
                                text = stringResource(id = R.string.create),
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.my_lists),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    navigationIcon = {
                        when {
                            creating -> {
                                IconButton(
                                    onClick = { listsUiEvent(ListsUiEvent.CreateMode(false)) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Close"
                                    )
                                }
                            }
                            canNavigateBack -> {
                                IconButton(
                                    onClick = { listsUiEvent(ListsUiEvent.OnNavigateBack) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = "Go back"
                                    )
                                }
                            }
                            else -> Unit
                        }
                    },
                    actions = {
                        if (creating) {
                            IconButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    listsUiEvent(
                                        ListsUiEvent.CreateList(
                                            name = listsState().listName,
                                            description = listsState().listDescription,
                                            public = listsState().publicList
                                        )
                                    )
                                },
                                enabled = listsState().listName.isNotBlank() && !listsState().creating
                            ) {
                                AnimatedContent(
                                    targetState = listsState().creating,
                                    label = "Create animation"
                                ) { creating ->
                                    if (creating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            trackColor = Color.Transparent,
                                            strokeWidth = 2.dp,
                                            strokeCap = StrokeCap.Round
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (creating) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface,
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
                            if (creating) {
                                item(
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    CreateList(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        listsState = listsState,
                                        listsUiEvent = listsUiEvent
                                    )
                                }
                            } else {
                                item(
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateItem()
                                            .clip(MaterialTheme.shapes.large)
                                            .clickable {
                                                listsUiEvent(ListsUiEvent.CreateMode(true))
                                            }
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                                shape = MaterialTheme.shapes.large
                                            )
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(18.dp),
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = null
                                            )
                                            Text(
                                                text = stringResource(id = R.string.create_list),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                }
                                if (lists.isEmpty()) {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = stringResource(id = R.string.empty_lists),
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
                }
            )
        }
    }
}