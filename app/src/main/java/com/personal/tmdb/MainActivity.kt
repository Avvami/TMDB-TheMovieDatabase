package com.personal.tmdb

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.personal.tmdb.core.domain.util.DialogController
import com.personal.tmdb.core.domain.util.DialogEvent
import com.personal.tmdb.core.domain.util.ObserveAsEvents
import com.personal.tmdb.core.domain.util.SnackbarController
import com.personal.tmdb.core.domain.util.ToastController
import com.personal.tmdb.core.navigation.RootNavHost
import com.personal.tmdb.core.presentation.components.BottomBar
import com.personal.tmdb.core.presentation.components.CustomDialog
import com.personal.tmdb.ui.theme.TMDBTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.holdSplash
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val preferencesState by mainViewModel.preferencesState.collectAsStateWithLifecycle()
            val userState by mainViewModel.userState.collectAsStateWithLifecycle()
            val dialogState by mainViewModel.dialogState.collectAsStateWithLifecycle()
            TMDBTheme(
                darkTheme = preferencesState.darkTheme ?: isSystemInDarkTheme()
            ) {
                val rootNavController = rememberNavController()
                var navBarItemReselect: (() -> Unit)? by remember { mutableStateOf(null) }
                var bottomBarVisible by rememberSaveable { mutableStateOf(true) }
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                ObserveAsEvents(
                    flow = SnackbarController.events,
                    key1 = snackbarHostState
                ) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState.showSnackbar(
                            message = event.message.asString(this@MainActivity),
                            actionLabel = event.action?.name?.asString(this@MainActivity),
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }
                ObserveAsEvents(flow = DialogController.events) { event ->
                    mainViewModel.uiEvent(
                        UiEvent.ShowDialog(
                            show = true,
                            content = DialogEvent(
                                iconRes = event.iconRes,
                                title = event.title,
                                message = event.message,
                                dismissAction = event.dismissAction,
                                confirmAction = event.confirmAction
                            )
                        )
                    )
                }
                ObserveAsEvents(flow = ToastController.events) { message ->
                    Toast.makeText(this, message.asString(this), Toast.LENGTH_SHORT).show()
                }

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            rootNavController = rootNavController,
                            bottomBarVisible = bottomBarVisible,
                            preferencesState = { preferencesState },
                            userState = { userState },
                            navBarItemReselect = navBarItemReselect
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            snackbar = {
                                Snackbar(
                                    snackbarData = it,
                                    shape = MaterialTheme.shapes.medium,
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                    contentColor = MaterialTheme.colorScheme.surfaceContainer,
                                    actionContentColor = MaterialTheme.colorScheme.primary,
                                    actionColor = MaterialTheme.colorScheme.primary,
                                    dismissActionContentColor = MaterialTheme.colorScheme.surfaceContainer
                                )
                            }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.surface
                ) { innerPadding ->
                    val animatedBottomBarPadding by animateDpAsState(
                        targetValue = if (bottomBarVisible) {
                            innerPadding.calculateBottomPadding()
                        } else {
                            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                        }
                    )
                    RootNavHost(
                        rootNavController = rootNavController,
                        navBarItemReselect = { navBarItemReselect = it },
                        setBottomBarVisible = { visible -> bottomBarVisible = visible },
                        bottomBarInsets = WindowInsets(bottom = animatedBottomBarPadding),
                        bottomBarPadding = innerPadding.calculateBottomPadding(),
                        preferencesState = { preferencesState },
                        userState = { userState },
                        uiEvent = mainViewModel::uiEvent
                    )
                }

                CustomDialog(
                    dialogState = dialogState,
                    onDismissRequest = { mainViewModel.uiEvent(UiEvent.ShowDialog(false)) },
                    showDialog = mainViewModel.showDialog
                )
            }
        }
    }

    fun openCustomChromeTab(url: String) {
        val chromeIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.backgroundLight))
                .build())
            .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.backgroundDark))
                .build())
            .setUrlBarHidingEnabled(true)
            .setShowTitle(true)
            .build()
        chromeIntent.launchUrl(this, url.toUri())
    }
}

fun Activity.openLink(url: String) {
    Intent(Intent.ACTION_VIEW).apply {
        data = url.toUri()
    }.also { intent ->
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, R.string.application_not_found, Toast.LENGTH_SHORT).show()
        }
    }
}