package com.personal.tmdb.core.presentation.add_to_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.UiTextException
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.add_to_list.components.MyListCard
import com.personal.tmdb.core.presentation.add_to_list.components.MyListCardShimmer
import com.personal.tmdb.core.presentation.components.MessageContainer

@Composable
fun AddToListScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: AddToListViewModel = hiltViewModel()
) {
    val addToListState by viewModel.addToListState.collectAsStateWithLifecycle()
    AddToListScreen(
        addToListState = addToListState,
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
    addToListState: AddToListState,
    addToListUiEvent: (AddToListUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.add_to_list),
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { addToListUiEvent(AddToListUiEvent.OnNavigateBack) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow_back_fill0_wght400),
                            contentDescription = null
                        )
                    }
                },
                windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout)
            )
        },
        floatingActionButton = {
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
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        val listsResults = addToListState.myListsResults?.collectAsLazyPagingItems()
        if (listsResults?.loadState?.refresh is LoadState.Error) {
            val error = (listsResults.loadState.refresh as LoadState.Error).error
            MessageContainer(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
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
                    listsResults.retry()
                }
            )
        } else {
            AnimatedContent(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                targetState = listsResults?.let {
                    it.loadState.refresh is LoadState.Loading && it.itemCount == 0
                } ?: true,
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { loading ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    item {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                        ) {
                            if (loading) {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(MaterialTheme.shapes.medium)
                                        .shimmerEffect(),
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.Transparent
                                    ),
                                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier.size(22.dp),
                                        painter = painterResource(R.drawable.icon_add_fill0_wght400),
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                                    Text(
                                        text = stringResource(id = R.string.create_list),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            } else {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { /*TODO*/ },
                                    shape = MaterialTheme.shapes.medium,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier.size(22.dp),
                                        painter = painterResource(R.drawable.icon_add_fill0_wght400),
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                                    Text(
                                        text = stringResource(id = R.string.create_list),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                    if (loading) {
                        items(count = 5) {
                            MyListCardShimmer()
                        }
                    } else {
                        listsResults?.let { results ->
                            if (results.itemCount == 0) {
                                item {
                                    MessageContainer(
                                        modifier = Modifier.fillMaxWidth(),
                                        content = {
                                            Text(
                                                text = stringResource(id = R.string.empty_lists),
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
                                    key = results.itemKey { it.id },
                                    contentType = results.itemContentType { "List" }
                                ) { index ->
                                    results[index]?.let { myList ->
                                        MyListCard(
                                            myList = myList,
                                            addToListUiEvent = addToListUiEvent
                                        )
                                    }
                                }
                                when (results.loadState.append) {
                                    is LoadState.Error -> {
                                        val error = (results.loadState.append as LoadState.Error).error
                                        item {
                                            MessageContainer(
                                                modifier = Modifier.fillMaxWidth(),
                                                content = {
                                                    Text(
                                                        text = when (error) {
                                                            is UiTextException -> error.uiText.asString()
                                                            else -> error.localizedMessage
                                                                ?: stringResource(id = R.string.error_unknown)
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
                                        items(count = 1) {
                                            MyListCardShimmer()
                                        }
                                    }
                                    else -> Unit
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