package com.personal.tmdb.home.presentation.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.UiTextException
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.FlexibleTopAppBar
import com.personal.tmdb.core.presentation.components.FlexibleTopBarDefaults
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.core.presentation.components.MessageContainer
import com.personal.tmdb.discover.presentation.discover.components.DiscoverTabs
import com.personal.tmdb.home.presentation.home.components.HomeBanner
import com.personal.tmdb.home.presentation.home.components.HomeShimmer

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreenRoot(
    bottomPadding: Dp,
    lazyListState: LazyListState = rememberLazyListState(),
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()
    with(sharedTransitionScope) {
        HomeScreen(
            modifier = Modifier.padding(bottom = bottomPadding),
            animatedContentScope = animatedContentScope,
            lazyListState = lazyListState,
            preferencesState = preferencesState,
            homeState = { homeState },
            homeUiEvent = { event ->
                when (event) {
                    is HomeUiEvent.OnNavigateTo -> {
                        onNavigateTo(event.route)
                    }
                    else -> Unit
                }
                viewModel.homeUiEvent(event)
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.HomeScreen(
    modifier: Modifier = Modifier,
    animatedContentScope: AnimatedContentScope,
    lazyListState: LazyListState,
    preferencesState: () -> PreferencesState,
    homeState: () -> HomeState,
    homeUiEvent: (HomeUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            FlexibleTopAppBar(
                scrollBehavior = scrollBehavior,
                content = {
                    DiscoverTabs(
                        modifier = Modifier.padding(vertical = 16.dp),
                        preferencesState = preferencesState,
                        onTabSelected = { route ->
                            homeUiEvent(HomeUiEvent.OnNavigateTo(route))
                        },
                        uiState = MediaType.UNKNOWN,
                        animatedContentScope = animatedContentScope
                    )
                },
                colors = FlexibleTopBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(.8f)
                )
            )
        }
    ) { innerPadding ->
        val animatedColor by animateColorAsState(
            targetValue = homeState().randomMediaDominantColors?.color ?: MaterialTheme.colorScheme.surface,
            label = "AnimatedShadowColor"
        )
        val trending = homeState().trending?.collectAsLazyPagingItems()
        val popularMovies = homeState().popularMovies?.collectAsLazyPagingItems()
        val popularTvShows = homeState().popularTvShows?.collectAsLazyPagingItems()
        val upcomingMovies = homeState().upcomingMovies?.collectAsLazyPagingItems()
        if (homeState().loading || trending?.loadState?.refresh is LoadState.Loading
            || popularMovies?.loadState?.refresh is LoadState.Loading
            || popularTvShows?.loadState?.refresh is LoadState.Loading
            || upcomingMovies?.loadState?.refresh is LoadState.Loading) {
            HomeShimmer(
                modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
                preferencesState = preferencesState
            )
        } else {
            if (homeState().errorMessage != null || trending?.loadState?.refresh is LoadState.Error
                || popularMovies?.loadState?.refresh is LoadState.Error
                || popularTvShows?.loadState?.refresh is LoadState.Error
                || upcomingMovies?.loadState?.refresh is LoadState.Error) {
                MessageContainer(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                        .padding(16.dp),
                    content = {
                        val error = homeState().errorMessage ?: (trending?.loadState?.refresh as? LoadState.Error)?.error
                            ?: (popularMovies?.loadState?.refresh as? LoadState.Error)?.error
                            ?: (popularTvShows?.loadState?.refresh as? LoadState.Error)?.error
                            ?: (upcomingMovies?.loadState?.refresh as? LoadState.Error)?.error
                        Text(
                            text = stringResource(id = R.string.error_general),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = when (error) {
                                is UiTextException -> error.uiText.asString()
                                is UiText -> error.asString()
                                else -> stringResource(id = R.string.error_unknown)
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    },
                    onRetry = {
                        homeUiEvent(HomeUiEvent.RetryRequests)
                    }
                )
            } else {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    state = lazyListState
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            animatedColor,
                                            Color.Transparent
                                        )
                                    ),
                                    alpha = .6f
                                )
                                .padding(top = innerPadding.calculateTopPadding())
                        ) {
                            HomeBanner(
                                modifier = Modifier
                                    .height(450.dp)
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                        shape = MaterialTheme.shapes.extraLarge
                                    ),
                                preferencesState = preferencesState,
                                homeState = homeState,
                                homeUiEvent = homeUiEvent
                            )
                        }
                    }
                    trending?.let { trending ->
                        item {
                            MediaCarousel(
                                titleContent = {
                                    Text(text = stringResource(id = R.string.trending_today))
                                },
                                items = {
                                    items(
                                        count = trending.itemCount,
                                        key = trending.itemKey { it.uuid },
                                        contentType = trending.itemContentType { "Poster" }
                                    ) { index ->
                                        trending[index]?.let { mediaInfo ->
                                            MediaPoster(
                                                onNavigateTo = { homeUiEvent(HomeUiEvent.OnNavigateTo(it)) },
                                                mediaInfo = mediaInfo,
                                                mediaType = mediaInfo.mediaType,
                                                showTitle = preferencesState().showTitle,
                                                showVoteAverage = preferencesState().showVoteAverage,
                                            )
                                        }
                                    }
                                    when (trending.loadState.append) {
                                        LoadState.Loading -> {
                                            item {
                                                MediaPosterShimmer(showTitle = preferencesState().showTitle)
                                            }
                                        }
                                        else -> Unit
                                    }
                                }
                            )
                        }
                    }
                    popularMovies?.let { movies ->
                        item {
                            MediaCarousel(
                                titleContent = {
                                    Text(text = stringResource(id = R.string.popular_movies))
                                },
                                items = {
                                    items(
                                        count = movies.itemCount,
                                        key = movies.itemKey { it.uuid },
                                        contentType = movies.itemContentType { "Poster" }
                                    ) { index ->
                                        movies[index]?.let { mediaInfo ->
                                            MediaPoster(
                                                onNavigateTo = { homeUiEvent(HomeUiEvent.OnNavigateTo(it)) },
                                                mediaInfo = mediaInfo,
                                                mediaType = MediaType.MOVIE,
                                                showTitle = preferencesState().showTitle,
                                                showVoteAverage = preferencesState().showVoteAverage
                                            )
                                        }
                                    }
                                    when (movies.loadState.append) {
                                        LoadState.Loading -> {
                                            item {
                                                MediaPosterShimmer(showTitle = preferencesState().showTitle)
                                            }
                                        }
                                        else -> Unit
                                    }
                                }
                            )
                        }
                    }
                    popularTvShows?.let { shows ->
                        item {
                            MediaCarousel(
                                titleContent = {
                                    Text(text = stringResource(id = R.string.popular_tv))
                                },
                                items = {
                                    items(
                                        count = shows.itemCount,
                                        key = shows.itemKey { it.uuid },
                                        contentType = shows.itemContentType { "Poster" }
                                    ) { index ->
                                        shows[index]?.let { mediaInfo ->
                                            MediaPoster(
                                                onNavigateTo = { homeUiEvent(HomeUiEvent.OnNavigateTo(it)) },
                                                mediaInfo = mediaInfo,
                                                mediaType = MediaType.MOVIE,
                                                showTitle = preferencesState().showTitle,
                                                showVoteAverage = preferencesState().showVoteAverage
                                            )
                                        }
                                    }
                                    when (shows.loadState.append) {
                                        LoadState.Loading -> {
                                            item {
                                                MediaPosterShimmer(showTitle = preferencesState().showTitle)
                                            }
                                        }
                                        else -> Unit
                                    }
                                }
                            )
                        }
                    }
                    upcomingMovies?.let { movies ->
                        item {
                            MediaCarousel(
                                titleContent = {
                                    Text(text = stringResource(id = R.string.upcoming_movies))
                                },
                                items = {
                                    items(
                                        count = movies.itemCount,
                                        key = movies.itemKey { it.uuid },
                                        contentType = movies.itemContentType { "Poster" }
                                    ) { index ->
                                        movies[index]?.let { mediaInfo ->
                                            MediaPoster(
                                                onNavigateTo = { homeUiEvent(HomeUiEvent.OnNavigateTo(it)) },
                                                mediaInfo = mediaInfo,
                                                mediaType = MediaType.MOVIE,
                                                showTitle = preferencesState().showTitle,
                                                showVoteAverage = preferencesState().showVoteAverage
                                            )
                                        }
                                    }
                                    when (movies.loadState.append) {
                                        LoadState.Loading -> {
                                            item {
                                                MediaPosterShimmer(showTitle = preferencesState().showTitle)
                                            }
                                        }
                                        else -> Unit
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}