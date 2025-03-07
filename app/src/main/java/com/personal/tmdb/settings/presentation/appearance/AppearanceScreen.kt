package com.personal.tmdb.settings.presentation.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.settings.presentation.appearance.components.AppearanceAdditionalNavItem
import com.personal.tmdb.settings.presentation.appearance.components.AppearancePoster
import com.personal.tmdb.settings.presentation.appearance.components.AppearanceTheme

@Composable
fun AppearanceScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    appearanceViewModel: AppearanceViewModel = hiltViewModel()
) {
    AppearanceScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        preferencesState = preferencesState,
        userState = userState,
        appearanceUiEvent = { event ->
            when (event) {
                AppearanceUiEvent.OnNavigateBack -> onNavigateBack()
                else -> Unit
            }
            appearanceViewModel.appearanceUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceScreen(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    appearanceUiEvent: (AppearanceUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.appearance),
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { appearanceUiEvent(AppearanceUiEvent.OnNavigateBack) }
                    )  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
        ) {
            item(
                contentType = { "Posters appearance" }
            ) {
                AppearancePoster(
                    preferencesState = preferencesState,
                    appearanceUiEvent = appearanceUiEvent
                )
            }
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                item(
                    contentType = { "Additional navigation bar item" }
                ) {
                    AppearanceAdditionalNavItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        preferencesState = preferencesState,
                        appearanceUiEvent = appearanceUiEvent
                    )
                }
            }
            item(
                contentType = { "Theme" }
            ) {
                AppearanceTheme(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    preferencesState = preferencesState,
                    appearanceUiEvent = appearanceUiEvent
                )
            }
        }
    }
}