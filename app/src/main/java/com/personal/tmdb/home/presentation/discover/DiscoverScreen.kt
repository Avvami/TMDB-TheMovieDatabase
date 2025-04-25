package com.personal.tmdb.home.presentation.discover

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.IconChip
import com.personal.tmdb.core.presentation.components.IconChipDefaults
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.home.presentation.components.DiscoverTabs

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
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.DiscoverScreen(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    animatedContentScope: AnimatedContentScope,
    discoverState: () -> DiscoverState,
    discoverUiEvent: (DiscoverUiEvent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        MediaGrid(
            modifier = modifier,
            contentPadding = PaddingValues(top = innerPadding.calculateTopPadding() + 16.dp, bottom = 16.dp),
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                DiscoverTabs(
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
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null
                                )
                            },
                            colors = IconChipDefaults.iconChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                iconContentColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
                            ),
                            shape = CircleShape
                        )
                    },
                    trailingContent = {
                        FilterChip(
                            selected = false,
                            onClick = { /*TODO*/ },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.genres)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                labelColor = MaterialTheme.colorScheme.surfaceVariant,
                                iconColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = null,
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        )
                        IconChip(
                            onClick = { /*TODO*/ },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(IconChipDefaults.IconSize),
                                    painter = painterResource(id = R.drawable.icon_page_info_fill0_wght400),
                                    contentDescription = null
                                )
                            },
                            colors = IconChipDefaults.iconChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                iconContentColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            border = null
                        )
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(.7f))
                .statusBarsPadding()
        )
    }
}