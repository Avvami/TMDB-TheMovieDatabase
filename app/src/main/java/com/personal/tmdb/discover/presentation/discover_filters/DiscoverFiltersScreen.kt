package com.personal.tmdb.discover.presentation.discover_filters

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.discover.presentation.discover_filters.components.AirDatesFilter
import com.personal.tmdb.discover.presentation.discover_filters.components.ContentOriginFilter
import com.personal.tmdb.discover.presentation.discover_filters.components.FilterTabs
import com.personal.tmdb.discover.presentation.discover_filters.components.IncludeAdultFilter
import com.personal.tmdb.discover.presentation.discover_filters.components.RatingFilter
import com.personal.tmdb.discover.presentation.discover_filters.components.RuntimeFilter

@Composable
fun DiscoverFiltersScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: DiscoverFiltersViewModel = hiltViewModel()
) {
    val filtersState by viewModel.filtersState.collectAsStateWithLifecycle()
    DiscoverFiltersScreen(
        filtersState = { filtersState },
        filtersUiEvent = { event ->
            when (event) {
                DiscoverFiltersUiEvent.OnNavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.filtersUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverFiltersScreen(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.filters),
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { filtersUiEvent(DiscoverFiltersUiEvent.OnNavigateBack) }
                    )  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = FiltersState().hasChangesComparedTo(filtersState()),
                        enter = fadeIn(tween(100)),
                        exit = fadeOut(tween(100))
                    ) {
                        TextButton(
                            onClick = { filtersUiEvent(DiscoverFiltersUiEvent.ClearAll) }
                        ) {
                            Text(text = stringResource(id = R.string.clear_all))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            FilterTabs(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .verticalScroll(rememberScrollState()),
                filtersState = filtersState,
                filtersUiEvent = filtersUiEvent
            )
            AnimatedContent(
                modifier = Modifier.weight(1f),
                targetState = filtersState().filtersUi,
                label = "FilterUiTabSwitchAnimation"
            ) { ui ->
                when (ui) {
                    FiltersUi.RATING -> {
                        RatingFilter(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            filtersState = filtersState,
                            filtersUiEvent = filtersUiEvent
                        )
                    }
                    FiltersUi.AIR_DATES -> {
                        AirDatesFilter(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            filtersState = filtersState,
                            filtersUiEvent = filtersUiEvent
                        )
                    }
                    FiltersUi.RUNTIME -> {
                        RuntimeFilter(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            filtersState = filtersState,
                            filtersUiEvent = filtersUiEvent
                        )
                    }
                    FiltersUi.INCLUDE_ADULT -> {
                        IncludeAdultFilter(
                            modifier = Modifier.fillMaxWidth(),
                            filtersState = filtersState,
                            filtersUiEvent = filtersUiEvent
                        )
                    }
                    FiltersUi.CONTENT_ORIGIN -> {
                        ContentOriginFilter(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            filtersState = filtersState,
                            filtersUiEvent = filtersUiEvent
                        )
                    }
                }
            }
        }
    }
}