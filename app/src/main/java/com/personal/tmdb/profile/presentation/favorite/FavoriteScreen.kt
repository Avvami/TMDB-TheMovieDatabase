package com.personal.tmdb.profile.presentation.favorite

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.SnackbarController
import com.personal.tmdb.core.domain.util.SnackbarEvent
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.negativeHorizontalPadding
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.profile.presentation.favorite.components.FavoriteFilterChips
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreenRoot(
    bottomPadding: Dp,
    canNavigateBack: Boolean = true,
    lazyGridState: LazyGridState,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val favoriteState by viewModel.favoriteState.collectAsStateWithLifecycle()
    FavoriteScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        canNavigateBack = canNavigateBack,
        lazyGridState = lazyGridState,
        favoriteState = { favoriteState },
        preferencesState = preferencesState,
        favoriteUiEvent = { event ->
            when (event) {
                FavoriteUiEvent.OnNavigateBack -> onNavigateBack()
                is FavoriteUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                else -> Unit
            }
            viewModel.favoriteUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteScreen(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
    lazyGridState: LazyGridState,
    favoriteState: () -> FavoriteState,
    preferencesState: () -> PreferencesState,
    favoriteUiEvent: (FavoriteUiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        if (!lazyGridState.canScrollBackward) {
            favoriteUiEvent(FavoriteUiEvent.GetFavorites(favoriteState().mediaType, 1))
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.favorite),
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(
                            onClick = { favoriteUiEvent(FavoriteUiEvent.OnNavigateBack) }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        MediaGrid(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
            lazyGridState = lazyGridState,
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
            span = {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    FavoriteFilterChips(
                        modifier = Modifier
                            .negativeHorizontalPadding((-16).dp)
                            .padding(bottom = 8.dp)
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        favoriteState = favoriteState,
                        favoriteUiEvent = favoriteUiEvent
                    )
                }
            },
            items = {
                if (favoriteState().loading && favoriteState().favorite == null) {
                    items(
                        count = 15,
                        contentType = { "Poster" }
                    ) {
                        MediaPosterShimmer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            height = Dp.Unspecified,
                            showTitle = preferencesState().showTitle,
                        )
                    }
                } else {
                    favoriteState().errorMessage?.let {  }
                    favoriteState().favorite?.results?.let { favorite ->
                        if (favorite.isEmpty()) {
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.empty_favorites),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(
                                items = favorite,
                                key = { it.id }
                            ) { mediaInfo ->
                                MediaPoster(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem(),
                                    onNavigateTo = { favoriteUiEvent(FavoriteUiEvent.OnNavigateTo(it)) },
                                    onLongClick = {
                                        scope.launch {
                                            SnackbarController.sendEvent(
                                                event = SnackbarEvent(
                                                    message = UiText.StringResource(R.string.watchlist_sorry)
                                                )
                                            )
                                        }
                                    },
                                    height = Dp.Unspecified,
                                    mediaInfo = mediaInfo,
                                    mediaType = favoriteState().mediaType,
                                    showTitle = preferencesState().showTitle,
                                    showVoteAverage = preferencesState().showVoteAverage
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}