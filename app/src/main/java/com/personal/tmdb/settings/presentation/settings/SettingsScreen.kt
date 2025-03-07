package com.personal.tmdb.settings.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.MainActivity
import com.personal.tmdb.R
import com.personal.tmdb.UiEvent
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.findActivity
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.settings.presentation.settings.components.About
import com.personal.tmdb.settings.presentation.settings.components.AppSettings

@Composable
fun SettingsScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    uiEvent: (UiEvent) -> Unit
) {
    val activity = LocalContext.current.findActivity() as MainActivity
    SettingsScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        preferencesState = preferencesState,
        userState = userState,
        settingsUiEvent = { event ->
            when (event) {
                SettingsUiEvent.OnNavigateBack -> onNavigateBack()
                is SettingsUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                is SettingsUiEvent.OpenLink -> activity.openCustomChromeTab(event.link)
            }
        },
        uiEvent = uiEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    settingsUiEvent: (SettingsUiEvent) -> Unit,
    uiEvent: (UiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { settingsUiEvent(SettingsUiEvent.OnNavigateBack) }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
        ) {
            item(
                contentType = { "App settings" }
            ) {
                AppSettings(
                    preferencesState = preferencesState,
                    settingsUiEvent = settingsUiEvent
                )
            }
            item(
                contentType = { "About" }
            ) {
                About(settingsUiEvent = settingsUiEvent)
            }
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                item(
                    contentType = { "SignOut" }
                ) {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                    ) {
                        TextButton(
                            modifier = Modifier
                                .width(200.dp)
                                .animateItem(),
                            onClick = { uiEvent(UiEvent.SignOut(userState().user)) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(text = stringResource(id = R.string.logout))
                        }
                    }
                }
            }
        }
    }
}