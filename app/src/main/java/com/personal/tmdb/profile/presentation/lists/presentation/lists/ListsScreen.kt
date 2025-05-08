package com.personal.tmdb.profile.presentation.lists.presentation.lists

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
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
import com.personal.tmdb.core.domain.util.DialogAction
import com.personal.tmdb.core.domain.util.DialogController
import com.personal.tmdb.core.domain.util.DialogEvent
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.components.CreateList
import com.personal.tmdb.core.presentation.components.ListItem
import com.personal.tmdb.core.presentation.components.ListItemShimmer
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.profile.presentation.lists.presentation.lists.components.ListScreenShimmer
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
    val errorColor = MaterialTheme.colorScheme.error
    BackHandler(enabled = !backPressHandled) {
        when {
            listsState().createEnabled -> {
                if (listsState().listName.isNotBlank() || listsState().listDescription.isNotBlank()) {
                    scope.launch {
                        DialogController.sendEvent(
                            event = DialogEvent(
                                title = UiText.StringResource(R.string.discard_edit),
                                message = UiText.StringResource(R.string.edit_list_warning),
                                dismissAction = DialogAction(
                                    name = UiText.StringResource(R.string.cancel),
                                    action = {}
                                ),
                                confirmAction = DialogAction(
                                    name = UiText.StringResource(R.string.discard),
                                    action = { listsUiEvent(ListsUiEvent.CreateMode(false)) },
                                    color = errorColor
                                )
                            )
                        )
                    }
                } else {
                    listsUiEvent(ListsUiEvent.CreateMode(false))
                }
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
        transitionSpec = { fadeIn(animationSpec = tween(durationMillis = 150)) togetherWith fadeOut(animationSpec = tween(durationMillis = 150)) }
    ) { createEnabled ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        when {
                            createEnabled -> {
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
                            createEnabled || listsState().selectEnabled -> {
                                IconButton(
                                    onClick = {
                                        when {
                                            createEnabled -> {
                                                if (listsState().listName.isNotBlank() || listsState().listDescription.isNotBlank()) {
                                                    scope.launch {
                                                        DialogController.sendEvent(
                                                            event = DialogEvent(
                                                                title = UiText.StringResource(R.string.discard_edit),
                                                                message = UiText.StringResource(R.string.edit_list_warning),
                                                                dismissAction = DialogAction(
                                                                    name = UiText.StringResource(R.string.cancel),
                                                                    action = {}
                                                                ),
                                                                confirmAction = DialogAction(
                                                                    name = UiText.StringResource(R.string.discard),
                                                                    action = { listsUiEvent(ListsUiEvent.CreateMode(false)) },
                                                                    color = errorColor
                                                                )
                                                            )
                                                        )
                                                    }
                                                } else {
                                                    listsUiEvent(ListsUiEvent.CreateMode(false))
                                                }
                                            }
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
                                    scope.launch {
                                        DialogController.sendEvent(
                                            event = DialogEvent(
                                                title = UiText.StringResource(R.string.delete_selection),
                                                message = UiText.StringResource(R.string.delete_item_warning),
                                                dismissAction = DialogAction(
                                                    name = UiText.StringResource(R.string.cancel),
                                                    action = {}
                                                ),
                                                confirmAction = DialogAction(
                                                    name = UiText.StringResource(R.string.delete),
                                                    action = {
                                                        listsUiEvent(ListsUiEvent.DeleteSelectedLists(listsState().selectedItems))
                                                    },
                                                    color = errorColor
                                                )
                                            )
                                        )
                                    }
                                },
                                enabled = !listsState().deleting
                            ) {
                                AnimatedContent(
                                    targetState = listsState().deleting,
                                    label = "Delete animation"
                                ) { deleting ->
                                    if (deleting) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(2.dp),
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
                        if (createEnabled) {
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
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(2.dp),
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
                        containerColor = if (createEnabled || listsState().selectEnabled) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface,
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
                contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                columns = GridCells.Adaptive(360.dp),
                items = {
                    if (listsState().loading && listsState().lists == null) {
                        ListScreenShimmer()
                    } else {
                        listsState().errorMessage?.let {  }
                        listsState().lists?.results?.let { lists ->
                            if (createEnabled) {
                                item(
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    CreateList(
                                        name = listsState()::listName,
                                        setName = { listsUiEvent(ListsUiEvent.SetListName(it)) },
                                        description = listsState()::listDescription,
                                        setDescription = { listsUiEvent(ListsUiEvent.SetListDescription(it)) }
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
                                    if (listsState().paging) {
                                        items(
                                            count = 4
                                        ) {
                                            ListItemShimmer()
                                        }
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