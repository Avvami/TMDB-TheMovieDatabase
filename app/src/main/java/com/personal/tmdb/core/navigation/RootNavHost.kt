package com.personal.tmdb.core.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.personal.tmdb.UiEvent
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.AdditionalNavigationItem
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.add_to_list.AddToListScreenRoot
import com.personal.tmdb.core.presentation.components.animatedComposable
import com.personal.tmdb.core.presentation.components.staticComposable
import com.personal.tmdb.core.presentation.welcome_back.WelcomeBackScreenRoot
import com.personal.tmdb.detail.presentation.cast.CastScreenRoot
import com.personal.tmdb.detail.presentation.collection.CollectionScreenRoot
import com.personal.tmdb.detail.presentation.detail.DetailScreenRoot
import com.personal.tmdb.detail.presentation.episode.EpisodeDetailsScreenRoot
import com.personal.tmdb.detail.presentation.episodes.EpisodesScreenRoot
import com.personal.tmdb.detail.presentation.image.ImageViewerScreenRoot
import com.personal.tmdb.detail.presentation.person.PersonScreenRoot
import com.personal.tmdb.detail.presentation.reviews.ReviewsScreenRoot
import com.personal.tmdb.home.presentation.home.HomeScreenRoot
import com.personal.tmdb.profile.presentation.favorite.FavoriteScreenRoot
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.ListDetailsScreenRoot
import com.personal.tmdb.profile.presentation.lists.presentation.lists.ListsScreenRoot
import com.personal.tmdb.profile.presentation.profile.ProfileScreenRoot
import com.personal.tmdb.profile.presentation.watchlist.WatchlistScreenRoot
import com.personal.tmdb.search.presentation.search.SearchScreenRoot
import com.personal.tmdb.settings.presentation.appearance.AppearanceScreenRoot
import com.personal.tmdb.settings.presentation.languages.LanguagesScreenRoot
import com.personal.tmdb.settings.presentation.settings.SettingsScreenRoot
import kotlinx.coroutines.launch

@Composable
fun RootNavHost(
    rootNavController: NavHostController,
    navBarItemReselect: ((() -> Unit)?) -> Unit,
    bottomBarPadding: Dp,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    uiEvent: (UiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val onNavigateBack: () -> Unit = {
        rootNavController.navigateUp()
    }

    NavHost(
        navController = rootNavController,
        startDestination = Route.Home::class
    ) {
        staticComposable<Route.Home> {
            val lazyListState = rememberLazyListState()
            val navController = rememberNavController()
            navBarItemReselect {
                val popped = navController.popBackStack(
                    destinationId = navController.graph.startDestinationId,
                    inclusive = false
                )
                if (!popped && lazyListState.canScrollBackward) {
                    scope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                }
            }
            ChildNavHost(
                rootNavController = rootNavController,
                navController = navController,
                scrollState = lazyListState,
                startDestination = Route.Home,
                bottomBarPadding = bottomBarPadding,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        staticComposable<Route.Search> {
            val lazyGridState = rememberLazyGridState()
            val navController = rememberNavController()
            navBarItemReselect {
                val popped = navController.popBackStack(
                    destinationId = navController.graph.startDestinationId,
                    inclusive = false
                )
                if (!popped && lazyGridState.canScrollBackward) {
                    scope.launch {
                        lazyGridState.animateScrollToItem(0)
                    }
                }
            }
            ChildNavHost(
                rootNavController = rootNavController,
                navController = navController,
                scrollState = lazyGridState,
                startDestination = Route.Search,
                bottomBarPadding = bottomBarPadding,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        staticComposable<Route.Watchlist> {
            val lazyGridState = rememberLazyGridState()
            val navController = rememberNavController()
            navBarItemReselect {
                val popped = navController.popBackStack(
                    destinationId = navController.graph.startDestinationId,
                    inclusive = false
                )
                if (!popped && lazyGridState.canScrollBackward) {
                    scope.launch {
                        lazyGridState.animateScrollToItem(0)
                    }
                }
            }
            ChildNavHost(
                rootNavController = rootNavController,
                navController = navController,
                scrollState = lazyGridState,
                startDestination = Route.Watchlist,
                bottomBarPadding = bottomBarPadding,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        staticComposable<Route.Favorite> {
            val lazyGridState = rememberLazyGridState()
            val navController = rememberNavController()
            navBarItemReselect {
                val popped = navController.popBackStack(
                    destinationId = navController.graph.startDestinationId,
                    inclusive = false
                )
                if (!popped && lazyGridState.canScrollBackward) {
                    scope.launch {
                        lazyGridState.animateScrollToItem(0)
                    }
                }
            }
            ChildNavHost(
                rootNavController = rootNavController,
                navController = navController,
                scrollState = lazyGridState,
                startDestination = Route.Favorite,
                bottomBarPadding = bottomBarPadding,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        staticComposable<Route.MyLists> {
            val lazyGridState = rememberLazyGridState()
            val navController = rememberNavController()
            navBarItemReselect {
                val popped = navController.popBackStack(
                    destinationId = navController.graph.startDestinationId,
                    inclusive = false
                )
                if (!popped && lazyGridState.canScrollBackward) {
                    scope.launch {
                        lazyGridState.animateScrollToItem(0)
                    }
                }
            }
            ChildNavHost(
                rootNavController = rootNavController,
                navController = navController,
                scrollState = lazyGridState,
                startDestination = Route.MyLists,
                bottomBarPadding = bottomBarPadding,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        staticComposable<Route.Profile> {
            val lazyListState = rememberLazyListState()
            val navController = rememberNavController()
            navBarItemReselect {
                val popped = navController.popBackStack(
                    destinationId = navController.graph.startDestinationId,
                    inclusive = false
                )
                if (!popped && lazyListState.canScrollBackward) {
                    scope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                }
            }
            ChildNavHost(
                rootNavController = rootNavController,
                navController = navController,
                scrollState = lazyListState,
                startDestination = Route.Profile,
                bottomBarPadding = bottomBarPadding,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        animatedComposable<Route.Image> {
            ImageViewerScreenRoot(
                onNavigateBack = onNavigateBack,
                preferencesState = preferencesState
            )
        }
        composable<Route.AddToList>(
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            popExitTransition = { ExitTransition.None },
        ) {
            AddToListScreenRoot(
                onNavigateBack = onNavigateBack
            )
        }
        staticComposable<Route.WelcomeBack>(
            deepLinks = listOf(
                navDeepLink<Route.WelcomeBack>(
                    basePath = C.REDIRECT_URL
                )
            )
        ) {
            LaunchedEffect(true) {
                if (userState().user?.sessionId.isNullOrEmpty()) {
                    uiEvent(UiEvent.SignInUser)
                }
            }
            WelcomeBackScreenRoot(
                onNavigateBack = onNavigateBack,
                userState = userState
            )
        }
    }
}

@Composable
fun ChildNavHost(
    rootNavController: NavHostController,
    navController: NavHostController,
    scrollState: Any,
    startDestination: Route,
    bottomBarPadding: Dp,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    uiEvent: (UiEvent) -> Unit
) {
    val onNavigateBack: () -> Unit = {
        navController.navigateUp()
    }
    val onNavigateTo: (route: Route) -> Unit = { route ->
        val controller = if (route is Route.Image || route is Route.AddToList) rootNavController else navController
        controller.navigate(route = route) {
            launchSingleTop = if (route is Route.Image || route is Route.AddToList) true else route !is Route.Detail
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination::class
    ) {
        animatedComposable<Route.Home> {
            HomeScreenRoot(
                bottomPadding = bottomBarPadding,
                lazyListState = scrollState as LazyListState,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState
            )
        }
        animatedComposable<Route.Search> {
            SearchScreenRoot(
                bottomPadding = bottomBarPadding,
                lazyGridState = scrollState as LazyGridState,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState
            )
        }
        animatedComposable<Route.Watchlist> {
            WatchlistScreenRoot(
                bottomPadding = bottomBarPadding,
                canNavigateBack = preferencesState().additionalNavigationItem != AdditionalNavigationItem.WATCHLIST,
                lazyGridState = if (preferencesState().additionalNavigationItem == AdditionalNavigationItem.WATCHLIST) scrollState as LazyGridState else rememberLazyGridState(),
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState
            )
        }
        animatedComposable<Route.MyLists> {
            ListsScreenRoot(
                bottomPadding = bottomBarPadding,
                canNavigateBack = preferencesState().additionalNavigationItem != AdditionalNavigationItem.LISTS,
                lazyGridState = if (preferencesState().additionalNavigationItem == AdditionalNavigationItem.LISTS) scrollState as LazyGridState else rememberLazyGridState(),
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo
            )
        }
        animatedComposable<Route.Favorite> {
            FavoriteScreenRoot(
                bottomPadding = bottomBarPadding,
                canNavigateBack = preferencesState().additionalNavigationItem != AdditionalNavigationItem.FAVORITE,
                lazyGridState = if (preferencesState().additionalNavigationItem == AdditionalNavigationItem.FAVORITE) scrollState as LazyGridState else rememberLazyGridState(),
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState
            )
        }
        animatedComposable<Route.Profile> {
            ProfileScreenRoot(
                bottomPadding = bottomBarPadding,
                lazyListState = scrollState as LazyListState,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        animatedComposable<Route.Detail> {
            DetailScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState,
                userState = userState
            )
        }
        animatedComposable<Route.Reviews> {
            ReviewsScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack
            )
        }
        animatedComposable<Route.Episodes> {
            EpisodesScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo
            )
        }
        animatedComposable<Route.Episode> {
            EpisodeDetailsScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo,
                userState = userState
            )
        }
        animatedComposable<Route.Collection> {
            CollectionScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                preferencesState = preferencesState,
                onNavigateTo = onNavigateTo
            )
        }
        animatedComposable<Route.Cast> {
            CastScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo
            )
        }
        animatedComposable<Route.Person> {
            PersonScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState
            )
        }
        animatedComposable<Route.Settings> {
            SettingsScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState,
                userState = userState,
                uiEvent = uiEvent
            )
        }
        animatedComposable<Route.Appearance> {
            AppearanceScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                preferencesState = preferencesState,
                userState = userState
            )
        }
        animatedComposable<Route.Language> {
            LanguagesScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack
            )
        }
        animatedComposable<Route.Lost> {}
        animatedComposable<Route.ListDetails> {
            ListDetailsScreenRoot(
                bottomPadding = bottomBarPadding,
                onNavigateBack = onNavigateBack,
                onNavigateTo = onNavigateTo,
                preferencesState = preferencesState
            )
        }
    }
}
