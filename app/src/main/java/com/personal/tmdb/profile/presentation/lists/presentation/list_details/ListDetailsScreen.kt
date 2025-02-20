package com.personal.tmdb.profile.presentation.lists.presentation.list_details

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterRemove
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.components.EditList
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.components.ListCreator
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.components.ListDescription
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.components.ListDetailsScreenShimmer
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.components.ListMetadata

@Composable
fun ListDetailsScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    viewModel: ListDetailsViewModel = hiltViewModel()
) {
    val listDetailsState by viewModel.listDetailsState.collectAsStateWithLifecycle()
    ListDetailsScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        preferencesState = preferencesState,
        listDetailsState = { listDetailsState },
        listDetailsUiEvent = { event ->
            when (event) {
                ListDetailsUiEvent.OnNavigateBack -> onNavigateBack()
                is ListDetailsUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                else -> Unit
            }
            viewModel.listDetailsUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListDetailsScreen(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    listDetailsState: () -> ListDetailsState,
    listDetailsUiEvent: (ListDetailsUiEvent) -> Unit
) {
    BackHandler {
        if (listDetailsState().editing) {
            listDetailsUiEvent(ListDetailsUiEvent.ChangeEditListState)
        } else {
            listDetailsUiEvent(ListDetailsUiEvent.OnNavigateBack)
        }
    }
    AnimatedContent(
        targetState = listDetailsState().editing,
        label = "Edit list animation",
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { editing ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (editing) {
                            Text(
                                text = stringResource(id = R.string.edit),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    navigationIcon = {
                        if (editing) {
                            IconButton(
                                onClick = { listDetailsUiEvent(ListDetailsUiEvent.ChangeEditListState) }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Close"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { listDetailsUiEvent(ListDetailsUiEvent.OnNavigateBack) }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "Go back"
                                )
                            }
                        }
                    },
                    actions = {
                        if (editing) {
                            IconButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            val context = LocalContext.current
                            AnimatedVisibility(visible = listDetailsState().listDetails != null) {
                                IconButton(
                                    onClick = { listDetailsUiEvent(ListDetailsUiEvent.ChangeEditListState) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = null
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    context.shareText(C.SHARE_LIST.format(listDetailsState().listId))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Share,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (editing) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) { innerPadding ->
            if (listDetailsState().loading && listDetailsState().listDetails == null) {
                ListDetailsScreenShimmer(
                    modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
                    preferencesState = preferencesState
                )
            } else {
                listDetailsState().listDetails?.let { listDetails ->
                    MediaGrid(
                        modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
                        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
                        span = {
                            if (editing) {
                                item(
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    EditList(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        listDetailsState = listDetailsState,
                                        listDetailsUiEvent = listDetailsUiEvent
                                    )
                                }
                            } else {
                                listDetails.name?.let { name ->
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        Text(
                                            text = name,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                item(
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    ListDescription(
                                        modifier = Modifier.padding(top = 8.dp),
                                        description = listDetails.description
                                    )
                                }
                                listDetails.createdBy?.let { createdBy ->
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        ListCreator(
                                            modifier = Modifier.padding(top = 8.dp),
                                            createdBy = createdBy
                                        )
                                    }
                                }
                                item(
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    ListMetadata(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        listDetails = listDetails
                                    )
                                }
                            }
                        }
                    ) {
                        listDetails.results?.let { results ->
                            if (editing) {
                                items(
                                    items = results,
                                    key = { it.id }
                                ) { mediaInfo ->
                                    MediaPosterRemove(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateItem(),
                                        onClick = { listDetailsUiEvent(ListDetailsUiEvent.DeleteItem(mediaInfo.id)) },
                                        height = Dp.Unspecified,
                                        mediaInfo = mediaInfo,
                                        mediaType = mediaInfo.mediaType,
                                        showTitle = preferencesState().showTitle,
                                        showVoteAverage = preferencesState().showVoteAverage
                                    )
                                }
                            } else {
                                items(
                                    items = results,
                                    key = { it.id }
                                ) { mediaInfo ->
                                    MediaPoster(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateItem(),
                                        onNavigateTo = { listDetailsUiEvent(ListDetailsUiEvent.OnNavigateTo(it)) },
                                        height = Dp.Unspecified,
                                        mediaInfo = mediaInfo,
                                        mediaType = mediaInfo.mediaType,
                                        showTitle = preferencesState().showTitle,
                                        showVoteAverage = preferencesState().showVoteAverage
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}