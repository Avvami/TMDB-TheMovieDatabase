package com.personal.tmdb.detail.presentation.collection

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.negativeHorizontalPadding
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.detail.presentation.collection.components.CollectionMetadata
import com.personal.tmdb.detail.presentation.collection.components.CollectionScreenShimmer
import com.personal.tmdb.detail.presentation.collection.components.CollectionSortingChips

@Composable
fun CollectionScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    viewModel: CollectionViewModel = hiltViewModel()
) {
    val collectionState by viewModel.collectionState.collectAsStateWithLifecycle()
    CollectionScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        preferencesState = preferencesState,
        collectionState = { collectionState },
        collectionUiEvent = { event ->
            when (event) {
                CollectionUiEvent.OnNavigateBack -> onNavigateBack()
                is CollectionUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                else -> Unit
            }
            viewModel.collectionUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun CollectionScreen(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    collectionState: () -> CollectionState,
    collectionUiEvent: (CollectionUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { collectionUiEvent(CollectionUiEvent.OnNavigateBack) }
                    )  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    val context = LocalContext.current
                    IconButton(
                        onClick = {
                            context.shareText(C.SHARE_COLLECTION.format(collectionState().collectionId))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        if (collectionState().loading) {
            CollectionScreenShimmer(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                preferencesState = preferencesState
            )
        } else {
            collectionState().collectionInfo?.let { collectionInfo ->
                MediaGrid(
                    modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                    span = {
                        collectionInfo.name?.let { name ->
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
                        collectionInfo.overview?.let { overview ->
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                var collapsed by rememberSaveable { mutableStateOf(true) }
                                Text(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth()
                                        .animateContentSize()
                                        .clickable(
                                            interactionSource = null,
                                            indication = null
                                        ) {
                                            collapsed = !collapsed
                                        },
                                    text = overview,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = if (collapsed) 4 else Int.MAX_VALUE,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        collectionState().genres.takeIf { it.isNotEmpty() }?.let { genres ->
                            item(
                                span = { GridItemSpan(maxCurrentLineSpan) }
                            ) {
                                CompositionLocalProvider(
                                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
                                    LocalContentColor provides MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    FlowRow(
                                        modifier = Modifier
                                            .animateItem()
                                            .padding(top = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            text = stringResource(id = R.string.genres),
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                        genres.fastForEach { genre ->
                                            SuggestionChip(
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                onClick = { /*TODO: Navigate to genre screen*/ },
                                                label = {
                                                    Text(text = genre.name)
                                                },
                                                colors = SuggestionChipDefaults.suggestionChipColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                                    labelColor = MaterialTheme.colorScheme.surfaceVariant
                                                ),
                                                border = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item(
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            CollectionMetadata(
                                modifier = Modifier.padding(top = 8.dp),
                                collectionInfo = collectionInfo
                            )
                        }
                        item(
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            CollectionSortingChips(
                                modifier = Modifier
                                    .negativeHorizontalPadding((-16).dp)
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp),
                                collectionState = collectionState,
                                collectionUiEvent = collectionUiEvent
                            )
                        }
                    },
                    items = {
                        if (collectionInfo.parts.isNullOrEmpty()) {
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.empty_collection),
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        } else {
                            items(
                                items = collectionInfo.parts,
                                key = { it.id },
                                contentType = { "Poster" }
                            ) { part ->
                                MediaPoster(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem(),
                                    onNavigateTo = { collectionUiEvent(CollectionUiEvent.OnNavigateTo(it)) },
                                    height = Dp.Unspecified,
                                    mediaInfo = part,
                                    mediaType = MediaType.MOVIE,
                                    showTitle = preferencesState().showTitle,
                                    showVoteAverage = preferencesState().showVoteAverage
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}