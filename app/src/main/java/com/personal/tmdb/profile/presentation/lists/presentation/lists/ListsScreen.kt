package com.personal.tmdb.profile.presentation.lists.presentation.lists

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

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
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    BackHandler(enabled = !backPressHandled) {
        when {
            listsState().createEnabled -> {
                /*TODO: Show warning dialog*/
                listsUiEvent(ListsUiEvent.CreateMode(false))
            }
            listsState().selectEnabled -> listsUiEvent(ListsUiEvent.SetSelectEnabled(false))
            else -> {
                /*TODO: Find a better solution???*/
                backPressHandled = true
                scope.launch {
                    awaitFrame()
                    onBackPressedDispatcher?.onBackPressed()
                    backPressHandled = false
                }
            }
        }
    }
    LaunchedEffect(listsState().selectedItems) {
        if (listsState().selectedItems.isEmpty()) {
            listsUiEvent(ListsUiEvent.SetSelectEnabled(false))
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
                        when {
                            creating -> {
                                Text(
                                    text = stringResource(id = R.string.create),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            listsState().selectEnabled -> {
                                Text(
                                    text = stringResource(id = R.string.edit),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            else -> {
                                Text(
                                    text = stringResource(id = R.string.my_lists),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        when {
                            creating || listsState().selectEnabled -> {
                                IconButton(
                                    onClick = {
                                        when {
                                            creating -> listsUiEvent(ListsUiEvent.CreateMode(false))
                                            listsState().selectEnabled -> listsUiEvent(ListsUiEvent.SetSelectEnabled(false))
                                        }
                                    }
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
                        }
                    },
                    actions = {
                        if (listsState().selectedItems.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    listsUiEvent(ListsUiEvent.DeleteSelectedLists(listsState().selectedItems))
                                },
                                enabled = !listsState().deleting
                            ) {
                                AnimatedContent(
                                    targetState = listsState().deleting,
                                    label = "Delete animation"
                                ) { deleting ->
                                    if (deleting) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            trackColor = Color.Transparent,
                                            strokeWidth = 2.dp,
                                            strokeCap = StrokeCap.Round
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_delete_fill0_wght400),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
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
                        containerColor = if (creating || listsState().selectEnabled) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface,
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
                                            .clickable(
                                                enabled = !listsState().selectEnabled
                                            ) {
                                                listsUiEvent(ListsUiEvent.CreateMode(true))
                                            }
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                    alpha = .1f
                                                ),
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
                                            selectEnabled = { listsState().selectEnabled },
                                            multipleSelectEnabled = false,
                                            selected = { listsState().selectedItems.contains(listInfo) },
                                            onSelect = {
                                                if (listsState().selectedItems.contains(listInfo))
                                                    listsUiEvent(ListsUiEvent.RemoveSelectedItem(listInfo))
                                                else
                                                    listsUiEvent(ListsUiEvent.ReplaceSelectedItem(listInfo))
                                            },
                                            onLongClick = {
                                                listsUiEvent(ListsUiEvent.SetSelectEnabled(true))
                                                listsUiEvent(ListsUiEvent.AddSelectedItem(listInfo))
                                            },
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