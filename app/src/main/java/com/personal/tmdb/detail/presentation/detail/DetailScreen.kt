package com.personal.tmdb.detail.presentation.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.MainActivity
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.findActivity
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MessageContainer
import com.personal.tmdb.detail.presentation.components.ReviewBottomSheet
import com.personal.tmdb.detail.presentation.detail.components.ContentPage
import com.personal.tmdb.detail.presentation.detail.components.DetailedDescription
import com.personal.tmdb.detail.presentation.detail.components.MoreBottomSheet
import com.personal.tmdb.detail.presentation.detail.components.WatchProviders
import com.personal.tmdb.detail.presentation.detail.components.RatingBottomSheet
import com.personal.tmdb.openLink
import com.personal.tmdb.ui.theme.onSurfaceDark
import com.personal.tmdb.ui.theme.surfaceDark

@Composable
fun DetailScreenRoot(
    bottomBarInsets: WindowInsets,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.findActivity() as MainActivity
    val detailState by viewModel.detailState.collectAsStateWithLifecycle()
    DetailScreen(
        bottomBarInsets = bottomBarInsets,
        preferencesState = preferencesState,
        userState = userState,
        detailState = detailState,
        detailUiEvent = { event ->
            when (event) {
                DetailUiEvent.OnNavigateBack -> onNavigateBack()
                is DetailUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                DetailUiEvent.Share -> {
                    context.shareText(
                        C.SHARE_MEDIA.format(
                            detailState.mediaType.name.lowercase(),
                            detailState.mediaId
                        )
                    )
                }
                is DetailUiEvent.OpenUrl -> {
                    activity.openLink(event.url)
                }
                else -> Unit
            }
            viewModel.detailUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(
    modifier: Modifier = Modifier,
    bottomBarInsets: WindowInsets,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    BackHandler(enabled = detailState.uiState != DetailUiState.CONTENT) {
        when (detailState.uiState) {
            DetailUiState.CONTENT -> detailUiEvent(DetailUiEvent.OnNavigateBack)
            DetailUiState.WATCH_PROVIDERS -> detailUiEvent(DetailUiEvent.SetUiState(DetailUiState.CONTENT))
            DetailUiState.MORE_DETAILS -> detailUiEvent(DetailUiEvent.SetUiState(DetailUiState.CONTENT))
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = detailState.uiState == DetailUiState.CONTENT,
                        enter = slideInVertically(initialOffsetY = { -it / 5 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { -it / 5 })
                                + fadeOut(animationSpec = tween(90))
                    ) {
                        AnimatedVisibility(
                            visible = detailState.dimTopAppBar,
                            enter = slideInVertically(initialOffsetY = { it / 5 }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { -it / 5 })
                                    + fadeOut(animationSpec = tween(90))
                        ) {
                            Text(
                                text = detailState.details?.name ?: "",
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    AnimatedContent(
                        targetState = detailState.uiState,
                        transitionSpec = {
                            when (targetState) {
                                DetailUiState.CONTENT -> {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                        initialOffset = { it / 5 }
                                    ) + fadeIn() togetherWith fadeOut(animationSpec = tween(90))
                                }
                                else -> {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                        initialOffset = { it / 5 }
                                    ) + fadeIn() togetherWith fadeOut(animationSpec = tween(90))
                                }
                            }
                        }
                    ) { uiState ->
                        when (uiState) {
                            DetailUiState.CONTENT -> {
                                FilledIconButton(
                                    onClick = {
                                        detailUiEvent(DetailUiEvent.OnNavigateBack)
                                    },
                                    colors = IconButtonDefaults.filledIconButtonColors(
                                        containerColor = if (detailState.dimTopAppBar) {
                                            MaterialTheme.colorScheme.surface
                                        } else {
                                            surfaceDark
                                        },
                                        contentColor = if (detailState.dimTopAppBar) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else {
                                            onSurfaceDark
                                        }
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_arrow_back_fill0_wght400),
                                        contentDescription = null
                                    )
                                }
                            }
                            else -> {
                                IconButton(
                                    onClick = {
                                        detailUiEvent(DetailUiEvent.SetUiState(DetailUiState.CONTENT))
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_close_fill0_wght400),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = detailState.uiState == DetailUiState.CONTENT,
                        enter = slideInVertically(initialOffsetY = { -it / 5 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { -it / 5 })
                                + fadeOut(animationSpec = tween(durationMillis = 90))
                    ) {
                        AnimatedVisibility(
                            visible = detailState.dimTopAppBar,
                            enter = slideInVertically(initialOffsetY = { it / 5 }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { -it / 5 })
                                    + fadeOut(animationSpec = tween(durationMillis = 90))
                        ) {
                            IconButton(
                                onClick = { detailUiEvent(DetailUiEvent.Share) }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_share_fill0_wght400),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (detailState.dimTopAppBar) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        Color.Transparent
                    },
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout)
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        contentWindowInsets = WindowInsets.safeDrawing.union(bottomBarInsets)
    ) { innerPadding ->
        if (detailState.loadState is LoadState.Error) {
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
                        text = detailState.loadState.errorMessage.asString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        textAlign = TextAlign.Center
                    )
                },
                onRetry = {
                    detailUiEvent(DetailUiEvent.RetryRequest)
                }
            )
        } else {
            AnimatedContent(
                modifier = modifier.fillMaxSize(),
                targetState = detailState.uiState,
                transitionSpec = {
                    when (targetState) {
                        DetailUiState.CONTENT -> {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                initialOffset = { it / 20 }
                            ) + fadeIn() togetherWith fadeOut(animationSpec = tween(90))
                        }
                        else -> {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                initialOffset = { it / 20 }
                            ) + fadeIn() togetherWith fadeOut(animationSpec = tween(90))
                        }
                    }
                }
            ) { uiState ->
                when (uiState) {
                    DetailUiState.WATCH_PROVIDERS -> {
                        WatchProviders(
                            modifier = Modifier.padding(innerPadding),
                            detailState = detailState,
                            detailUiEvent = detailUiEvent
                        )
                    }
                    DetailUiState.MORE_DETAILS -> {
                        DetailedDescription(
                            modifier = Modifier.padding(innerPadding),
                            detailState = detailState,
                            userState = userState,
                            detailUiEvent = detailUiEvent
                        )
                    }
                    DetailUiState.CONTENT -> {
                        ContentPage(
                            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                            lazyListState = lazyListState,
                            preferencesState = preferencesState,
                            userState = userState,
                            detailState = detailState,
                            detailUiEvent = detailUiEvent
                        )
                    }
                }
            }
        }
    }
    if (detailState.showRatingSheet) {
        RatingBottomSheet(
            detailState = detailState,
            onDismissRequest = { detailUiEvent(DetailUiEvent.ShowRatingSheet(false)) },
            detailUiEvent = detailUiEvent
        )
    }
    if (detailState.selectedReview != null) {
        ReviewBottomSheet(
            review = detailState.selectedReview,
            onDismissRequest = { detailUiEvent(DetailUiEvent.OpenReview(null)) }
        )
    }
    if (detailState.showMoreSheet) {
        MoreBottomSheet(
            detailState = detailState,
            onDismissRequest = { detailUiEvent(DetailUiEvent.ShowMoreSheet(false)) },
            detailUiEvent = detailUiEvent
        )
    }
}