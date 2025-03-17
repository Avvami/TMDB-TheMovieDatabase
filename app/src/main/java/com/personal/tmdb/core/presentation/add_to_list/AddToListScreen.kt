package com.personal.tmdb.core.presentation.add_to_list

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.LoadingProgress
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.DialogAction
import com.personal.tmdb.core.domain.util.DialogController
import com.personal.tmdb.core.domain.util.DialogEvent
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.components.CreateList
import com.personal.tmdb.core.presentation.components.CustomListItem
import kotlinx.coroutines.launch

@Composable
fun AddToListScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: AddToListViewModel = hiltViewModel()
) {
    val addToListState by viewModel.addToListState.collectAsStateWithLifecycle()
    AddToListScreen(
        addToListState = { addToListState },
        addToListUiEvent = { event ->
            when (event) {
                AddToListUiEvent.OnNavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.addToListUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToListScreen(
    modifier: Modifier = Modifier,
    addToListState: () -> AddToListState,
    addToListUiEvent: (AddToListUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val errorColor = MaterialTheme.colorScheme.error
    BackHandler {
        if (addToListState().createEnabled) {
            if (addToListState().listName.isNotBlank() || addToListState().listDescription.isNotBlank()) {
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
                                action = { addToListUiEvent(AddToListUiEvent.CreateMode(false)) },
                                color = errorColor
                            )
                        )
                    )
                }
            } else {
                addToListUiEvent(AddToListUiEvent.CreateMode(false))
            }
        } else {
            addToListUiEvent(AddToListUiEvent.OnNavigateBack)
        }
    }
    AnimatedContent(
        targetState = addToListState().createEnabled,
        label = "Create list animation",
        transitionSpec = { fadeIn(animationSpec = tween(durationMillis = 150)) togetherWith fadeOut(animationSpec = tween(durationMillis = 150)) }
    ) { createEnabled ->
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        if (createEnabled) {
                            Text(
                                text = stringResource(id = R.string.create),
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.add_to_list),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (createEnabled) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    navigationIcon = {
                        if (createEnabled) {
                            IconButton(
                                onClick = {
                                    if (addToListState().listName.isNotBlank() || addToListState().listDescription.isNotBlank()) {
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
                                                        action = { addToListUiEvent(AddToListUiEvent.CreateMode(false)) },
                                                        color = errorColor
                                                    )
                                                )
                                            )
                                        }
                                    } else {
                                        addToListUiEvent(AddToListUiEvent.CreateMode(false))
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Close"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { addToListUiEvent(AddToListUiEvent.OnNavigateBack) }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = {
                        if (createEnabled) {
                            IconButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    addToListUiEvent(
                                        AddToListUiEvent.CreateList(
                                            name = addToListState().listName,
                                            description = addToListState().listDescription,
                                            public = addToListState().publicList
                                        )
                                    )
                                },
                                enabled = addToListState().listName.isNotBlank() && !addToListState().creating
                            ) {
                                AnimatedContent(
                                    targetState = addToListState().creating,
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
                    }
                )
            },
            floatingActionButton = {
                if (!createEnabled) {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                    ) {
                        Button(
                            onClick = { addToListUiEvent(AddToListUiEvent.OnNavigateBack) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.surface
                            ),
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.done),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier.padding(innerPadding)
            ) {
                if (createEnabled) {
                    item {
                        CreateList(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            name = addToListState()::listName,
                            setName = { addToListUiEvent(AddToListUiEvent.SetListName(it)) },
                            description = addToListState()::listDescription,
                            setDescription = { addToListUiEvent(AddToListUiEvent.SetListDescription(it)) }
                        )
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CompositionLocalProvider(
                                LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                            ) {
                                Button(
                                    onClick = { addToListUiEvent(AddToListUiEvent.CreateMode(true)) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.onSurface,
                                        contentColor = MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Icon(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                                    Text(text = stringResource(id = R.string.create_list))
                                }
                            }
                        }
                    }
                    item {
                        CustomListItem(
                            onClick = {
                                addToListUiEvent(AddToListUiEvent.UpdateList(favorite = !addToListState().favorite))
                            },
                            headlineContent = {
                                Text(
                                    text = stringResource(id = R.string.favorite),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            trailingContent = {
                                if (addToListState().favoriteLoading) {
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
                                    if (addToListState().favorite) {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_radio_button_unchecked_fill0_wght400),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                    }
                    item {
                        CustomListItem(
                            onClick = {
                                addToListUiEvent(AddToListUiEvent.UpdateList(watchlist = !addToListState().watchlist))
                            },
                            headlineContent = {
                                Text(
                                    text = stringResource(id = R.string.watchlist),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            trailingContent = {
                                if (addToListState().watchlistLoading) {
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
                                    if (addToListState().watchlist) {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_radio_button_unchecked_fill0_wght400),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                    }
                    item {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = stringResource(id = R.string.my_lists),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                    if (addToListState().loadingLists && addToListState().lists == null) {
                        items(
                            count = 10
                        ) {
                            CustomListItemShimmer()
                        }
                    } else {
                        addToListState().errorMessage?.let {  }
                        addToListState().lists?.let { lists ->
                            if (lists.results.isEmpty()) {
                                item {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        text = stringResource(id = R.string.empty_lists),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                items(
                                    items = lists.results,
                                    key = { it.id }
                                ) { listInfo ->
                                    CustomListItem(
                                        modifier = Modifier.animateItem(),
                                        onClick = {
                                            addToListUiEvent(AddToListUiEvent.AddToList(listInfo.id))
                                        },
                                        enabled = listInfo.loadingProgress == LoadingProgress.STILL,
                                        headlineContent = {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                AsyncImage(
                                                    modifier = Modifier
                                                        .height(48.dp)
                                                        .aspectRatio(1 / 1f)
                                                        .clip(MaterialTheme.shapes.extraSmall),
                                                    model = C.TMDB_IMAGES_BASE_URL + C.POSTER_W300 + listInfo.backdropPath,
                                                    placeholder = painterResource(id = R.drawable.placeholder),
                                                    error = painterResource(id = R.drawable.placeholder),
                                                    contentDescription = "Poster",
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(
                                                    text = listInfo.name ?: stringResource(id = R.string.empty),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        },
                                        trailingContent = {
                                            AnimatedContent(
                                                modifier = Modifier.defaultMinSize(minWidth = 24.dp, minHeight = 24.dp),
                                                targetState = listInfo.loadingProgress,
                                                label = "Add to list animation",
                                                transitionSpec = { scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut() }
                                            ) { loadingProgress ->
                                                when (loadingProgress) {
                                                    LoadingProgress.STILL -> Unit
                                                    LoadingProgress.LOADING -> {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                                .padding(2.dp),
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            trackColor = Color.Transparent,
                                                            strokeWidth = 2.dp,
                                                            strokeCap = StrokeCap.Round
                                                        )
                                                    }
                                                    LoadingProgress.SUCCESS -> {
                                                        Icon(
                                                            imageVector = Icons.Rounded.CheckCircle,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.primary
                                                        )
                                                    }
                                                    LoadingProgress.ERROR -> {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.icon_error_fill1_wght400),
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.error
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                                if (addToListState().paging) {
                                    items(
                                        count = 4
                                    ) {
                                        CustomListItemShimmer()
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .height(ButtonDefaults.MinHeight)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomListItemShimmer(
    modifier: Modifier = Modifier
) {
    CustomListItem(
        modifier = modifier,
        onClick = {},
        enabled = false,
        headlineContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .aspectRatio(1 / 1f)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                )
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    text = "Your (Elon's) personal list",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Transparent
                )
            }
        }
    )
}