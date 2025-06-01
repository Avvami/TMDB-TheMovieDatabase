package com.personal.tmdb.discover.presentation.discover_filters

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.discover.presentation.discover_filters.components.CountryFilter
import com.personal.tmdb.discover.presentation.discover_filters.components.Filters
import com.personal.tmdb.discover.presentation.discover_filters.components.SortBy

@Composable
fun DiscoverFiltersScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: DiscoverFiltersViewModel = hiltViewModel(),
    applyFilters: (filters: FiltersState) -> Unit
) {
    val filtersState by viewModel.filtersState.collectAsStateWithLifecycle()
    DiscoverFiltersScreen(
        filtersState = { filtersState },
        filtersUiEvent = { event ->
            when (event) {
                DiscoverFiltersUiEvent.OnNavigateBack -> onNavigateBack()
                DiscoverFiltersUiEvent.ApplyFilters -> {
                    applyFilters(filtersState)
                    onNavigateBack()
                }
                else -> Unit
            }
            viewModel.filtersUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun DiscoverFiltersScreen(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit
) {
    BackHandler(
        enabled = filtersState().filtersUi == FiltersUi.COUNTRY
                || filtersState().filtersUi == FiltersUi.YEAR
    ) {
        filtersUiEvent(DiscoverFiltersUiEvent.SetFiltersUi(FiltersUi.ALL))
    }
    SharedTransitionLayout {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        when (filtersState().filtersUi) {
                            FiltersUi.ALL -> {
                                Text(
                                    text = stringResource(id = R.string.filters),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            FiltersUi.COUNTRY -> {
                                Text(
                                    text = stringResource(id = R.string.country),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            FiltersUi.YEAR -> {
                                Text(
                                    text = stringResource(id = R.string.year),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                when (filtersState().filtersUi) {
                                    FiltersUi.ALL -> {
                                        filtersUiEvent(DiscoverFiltersUiEvent.OnNavigateBack)
                                    }
                                    FiltersUi.COUNTRY -> {
                                        filtersUiEvent(DiscoverFiltersUiEvent.SetFiltersUi(FiltersUi.ALL))
                                    }
                                    FiltersUi.YEAR -> {
                                        filtersUiEvent(DiscoverFiltersUiEvent.SetFiltersUi(FiltersUi.ALL))
                                    }
                                }
                            }
                        )  {
                            Icon(
                                painter = painterResource(R.drawable.icon_arrow_back_fill0_wght400),
                                contentDescription = "Go back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout)
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            contentWindowInsets = WindowInsets.safeDrawing.exclude(WindowInsets.ime)
        ) { innerPadding ->
            AnimatedContent(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                targetState = filtersState().filtersUi
            ) { ui ->
                when (ui) {
                    FiltersUi.COUNTRY -> {
                        CountryFilter(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                            filtersState = filtersState,
                            filtersUiEvent = filtersUiEvent,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this@AnimatedContent
                        )
                    }
                    FiltersUi.ALL -> {
                        Column(
                            modifier = modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Filters(
                                    filtersState = filtersState,
                                    filtersUiEvent = filtersUiEvent,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@AnimatedContent
                                )
                                SortBy(
                                    filtersState = filtersState,
                                    filtersUiEvent = filtersUiEvent
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        filtersUiEvent(DiscoverFiltersUiEvent.ResetFilters)
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                        disabledContentColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                    contentPadding = PaddingValues(
                                        horizontal = 32.dp,
                                        vertical = 12.dp
                                    )
                                ) {
                                    Text(
                                        text = stringResource(R.string.reset),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Button(
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        filtersUiEvent(DiscoverFiltersUiEvent.ApplyFilters)
                                    },
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(
                                        horizontal = 32.dp,
                                        vertical = 12.dp
                                    )
                                ) {
                                    Text(
                                        text = stringResource(R.string.apply),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                    FiltersUi.YEAR -> {

                    }
                }
            }
        }
    }
}