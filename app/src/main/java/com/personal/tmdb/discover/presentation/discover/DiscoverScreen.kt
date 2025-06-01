package com.personal.tmdb.discover.presentation.discover

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.FlexibleTopAppBar
import com.personal.tmdb.core.presentation.components.FlexibleTopBarDefaults
import com.personal.tmdb.core.presentation.components.IconChip
import com.personal.tmdb.core.presentation.components.IconChipDefaults
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.core.presentation.components.MessageContainer
import com.personal.tmdb.discover.presentation.discover.components.DiscoverTabs
import com.personal.tmdb.discover.presentation.discover.components.GenresDialog

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DiscoverScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val discoverState by viewModel.discoverState.collectAsStateWithLifecycle()
    with(sharedTransitionScope) {
        DiscoverScreen(
            modifier = Modifier.padding(bottom = bottomPadding),
            preferencesState = preferencesState,
            animatedContentScope = animatedContentScope,
            discoverState = { discoverState },
            discoverUiEvent = { event ->
                when (event) {
                    DiscoverUiEvent.OnNavigateBack -> onNavigateBack()
                    is DiscoverUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                    else -> Unit
                }
                viewModel.discoverUiEvent(event)
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.DiscoverScreen(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    animatedContentScope: AnimatedContentScope,
    discoverState: () -> DiscoverState,
    discoverUiEvent: (DiscoverUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FlexibleTopAppBar(
                scrollBehavior = scrollBehavior,
                content = {
                    DiscoverTabs(
                        modifier = Modifier.padding(vertical = 16.dp),
                        preferencesState = preferencesState,
                        uiState = discoverState().uiState,
                        animatedContentScope = animatedContentScope,
                        leadingContent = {
                            IconChip(
                                onClick = {
                                    discoverUiEvent(DiscoverUiEvent.OnNavigateBack)
                                },
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(IconChipDefaults.IconSize),
                                        painter = painterResource(R.drawable.icon_close_fill0_wght400),
                                        contentDescription = null
                                    )
                                },
                                colors = IconChipDefaults.iconChipColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                    iconContentColor = MaterialTheme.colorScheme.primary
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
                                ),
                                shape = CircleShape
                            )
                        },
                        trailingContent = {
                            FilterChip(
                                selected = false,
                                onClick = { discoverUiEvent(DiscoverUiEvent.SetShowGenresState(true)) },
                                label = {
                                    Text(
                                        text = discoverState().selectedGenre?.name ?: stringResource(id = R.string.genres)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                                    labelColor = MaterialTheme.colorScheme.surfaceVariant,
                                    iconColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                                ),
                                trailingIcon = {
                                    Icon(
                                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                                        painter = painterResource(R.drawable.icon_keyboard_arrow_down_fill0_wght400),
                                        contentDescription = null
                                    )
                                },
                                shape = CircleShape
                            )
                            IconChip(
                                onClick = { discoverUiEvent(DiscoverUiEvent.OnNavigateTo(Route.DiscoverFilters)) },
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(IconChipDefaults.IconSize),
                                        painter = painterResource(id = R.drawable.icon_page_info_fill0_wght400),
                                        contentDescription = null
                                    )
                                },
                                colors = IconChipDefaults.iconChipColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                                    iconContentColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                                ),
                                shape = CircleShape
                            )
                            IconChip(
                                onClick = { /*TODO*/ },
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(IconChipDefaults.IconSize),
                                        painter = painterResource(id = R.drawable.icon_swap_vert_fill0_wght400),
                                        contentDescription = null
                                    )
                                },
                                colors = IconChipDefaults.iconChipColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                                    iconContentColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                                ),
                                shape = CircleShape
                            )
                        }
                    )
                },
                subContent = {
                    Column {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = stringResource(R.string.filters),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp)
                        ) {
                            FilterChip(
                                selected = false,
                                onClick = {
                                    discoverUiEvent(DiscoverUiEvent.SetShowGenresState(true))
                                },
                                label = {
                                    Text(
                                        text = "Rated: 0 - 10"
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                                    labelColor = MaterialTheme.colorScheme.surfaceVariant,
                                    iconColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                                ),
                                trailingIcon = {
                                    Icon(
                                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                                        painter = painterResource(R.drawable.icon_close_fill0_wght400),
                                        contentDescription = null
                                    )
                                },
                                shape = CircleShape
                            )
                        }
                    }
                },
                colors = FlexibleTopBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .9f),
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = .9f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        val discover = discoverState().discover?.collectAsLazyPagingItems()
        if (discover?.loadState?.refresh is LoadState.Error) {
            val error = (discover.loadState.refresh as LoadState.Error).error
            MessageContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
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
                    discover.retry()
                }
            )
        } else {
            MediaGrid(
                modifier = modifier,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                items = {
                    if (discoverState().loading
                        || discover?.loadState?.refresh is LoadState.Loading) {
                        items(
                            count = 20,
                            contentType = { "Poster" }
                        ) {
                            MediaPosterShimmer(
                                modifier = Modifier.fillMaxWidth(),
                                height = Dp.Unspecified,
                                showTitle = preferencesState().showTitle,
                            )
                        }
                    } else {
                        discover?.let { results ->
                            if (results.itemCount == 0) {
                                item(
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    MessageContainer(
                                        modifier = Modifier.fillMaxWidth(),
                                        content = {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = stringResource(id = R.string.empty_discover),
                                                style = MaterialTheme.typography.bodyLarge,
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
                                            onNavigateTo = {
                                                discoverUiEvent(DiscoverUiEvent.OnNavigateTo(it))
                                            },
                                            height = Dp.Unspecified,
                                            mediaInfo = mediaInfo,
                                            mediaType = discoverState().uiState,
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
            )
        }
        if (discoverState().showGenres) {
            GenresDialog(
                discoverState = discoverState,
                onDismissRequest = { discoverUiEvent(DiscoverUiEvent.SetShowGenresState(false)) },
                selectGenre = { genre -> discoverUiEvent(DiscoverUiEvent.SetGenre(genre)) }
            )
        }
    }
}