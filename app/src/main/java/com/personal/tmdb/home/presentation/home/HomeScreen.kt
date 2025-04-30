package com.personal.tmdb.home.presentation.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaBanner
import com.personal.tmdb.core.presentation.components.MediaBannerShimmer
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.discover.presentation.discover.components.DiscoverTabs
import com.personal.tmdb.home.presentation.home.components.HomeBanner

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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.HomeScreen(
    modifier: Modifier = Modifier,
    animatedContentScope: AnimatedContentScope,
    lazyListState: LazyListState,
    preferencesState: () -> PreferencesState,
    homeState: () -> HomeState,
    homeUiEvent: (HomeUiEvent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        val animatedColor by animateColorAsState(
            targetValue = homeState().randomMediaDominantColors?.color ?: MaterialTheme.colorScheme.surface,
            label = "AnimatedShadowColor"
        )
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
                                colors = listOf(animatedColor.copy(alpha = .5f), Color.Transparent)
                            )
                        )
                        .padding(top = innerPadding.calculateTopPadding() + 16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DiscoverTabs(
                            preferencesState = preferencesState,
                            onTabSelected = { route ->
                                homeUiEvent(HomeUiEvent.OnNavigateTo(route))
                            },
                            uiState = MediaType.UNKNOWN,
                            animatedContentScope = animatedContentScope
                        )
                        HomeBanner(
                            modifier = Modifier
                                .height(450.dp)
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                    shape = MaterialTheme.shapes.extraLarge
                                ),
                            preferencesState = preferencesState,
                            homeState = homeState,
                            homeUiEvent = homeUiEvent
                        )
                    }
                }
            }
            item {
                MediaCarousel(
                    titleContent = {
                        Text(text = stringResource(id = R.string.trending_today))
                    },
                    items = {
                        if (homeState().trending == null) {
                            items(15) {
                                MediaPosterShimmer(showTitle = preferencesState().showTitle)
                            }
                        } else {
                            homeState().trending?.results?.let { trending ->
                                items(
                                    items = trending,
                                    key = { it.id }
                                ) { mediaInfo ->
                                    MediaPoster(
                                        onNavigateTo = { homeUiEvent(HomeUiEvent.OnNavigateTo(it)) },
                                        mediaInfo = mediaInfo,
                                        mediaType = mediaInfo.mediaType,
                                        showTitle = preferencesState().showTitle,
                                        showVoteAverage = preferencesState().showVoteAverage,
                                    )
                                }
                            }
                        }
                    }
                )
            }
            item {
                MediaCarousel(
                    titleContent = {
                        Text(text = stringResource(id = R.string.now_playing))
                    },
                    items = {
                        if (homeState().nowPlaying == null) {
                            items(15) {
                                MediaBannerShimmer()
                            }
                        } else {
                            homeState().nowPlaying?.results?.let { nowPlaying ->
                                items(
                                    items = nowPlaying,
                                    key = { it.id }
                                ) { mediaInfo ->
                                    MediaBanner(
                                        onNavigateTo = { homeUiEvent(HomeUiEvent.OnNavigateTo(it)) },
                                        mediaInfo = mediaInfo,
                                        mediaType = MediaType.MOVIE,
                                        showVoteAverage = preferencesState().showVoteAverage
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
        val showStatusBarColor by remember {
            derivedStateOf {
                lazyListState.firstVisibleItemScrollOffset > 50
            }
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = showStatusBarColor,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(.7f))
                    .statusBarsPadding()
            )
        }
    }
}