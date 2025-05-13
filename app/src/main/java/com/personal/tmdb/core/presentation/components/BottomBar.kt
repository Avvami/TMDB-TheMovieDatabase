package com.personal.tmdb.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.AdditionalNavigationItem
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.navigation.NavigationItem
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState

@Composable
fun BottomBar(
    rootNavController: NavController,
    bottomBarVisible:Boolean,
    preferencesState: () -> PreferencesState,
    userState: () -> UserState,
    navBarItemReselect: (() -> Unit)?
) {
    val navigationItems = rememberNavigationItems(preferencesState, userState)

    AnimatedVisibility(
        visible = bottomBarVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        NavigationBarContent(
            navigationItems = navigationItems,
            rootNavController = rootNavController,
            navBarItemReselect = navBarItemReselect
        )
    }
}

@Composable
fun NavigationBarContent(
    navigationItems: List<NavigationItem>,
    rootNavController: NavController,
    navBarItemReselect: (() -> Unit)?
) {
    CustomNavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navigationItems.fastForEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
            CustomNavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        navBarItemReselect?.invoke()
                    } else {
                        rootNavController.navigate(item.route) {
                            popUpTo(rootNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (isSelected) item.selectedIcon() else item.unselectedIcon()
                },
                label = {
                    Text(text = stringResource(item.label))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                interactionSource = null
            )
        }
    }
}

@Composable
fun rememberNavigationItems(preferencesState: () -> PreferencesState, userState: () -> UserState): List<NavigationItem> {
    return remember(key1 = preferencesState().additionalNavigationItem, key2 = userState().user) {
        buildList {
            add(
                NavigationItem(
                    label = R.string.home,
                    unselectedIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_home_fill0_wght400),
                            contentDescription = stringResource(R.string.home)
                        )
                    },
                    selectedIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_home_fill1_wght400),
                            contentDescription = stringResource(R.string.home)
                        )
                    },
                    route = Route.Home
                )
            )
            add(
                NavigationItem(
                    label = R.string.search,
                    unselectedIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_search_fill0_wght400),
                            contentDescription = stringResource(R.string.search)
                        )
                    },
                    selectedIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_search_fill0_wght400),
                            contentDescription = stringResource(R.string.search)
                        )
                    },
                    route = Route.Search
                )
            )
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                when (preferencesState().additionalNavigationItem) {
                    AdditionalNavigationItem.WATCHLIST -> {
                        add(
                            NavigationItem(
                                label = R.string.watchlist,
                                unselectedIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_bookmark_fill0_wght400),
                                        contentDescription = stringResource(R.string.watchlist)
                                    )
                                },
                                selectedIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_bookmark_fill1_wght400),
                                        contentDescription = stringResource(R.string.watchlist)
                                    )
                                },
                                route = Route.Watchlist
                            )
                        )
                    }

                    AdditionalNavigationItem.FAVORITE -> {
                        add(
                            NavigationItem(
                                label = R.string.favorite,
                                unselectedIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_favorite_fill0_wght400),
                                        contentDescription = stringResource(R.string.favorite)
                                    )
                                },
                                selectedIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_favorite_fill1_wght400),
                                        contentDescription = stringResource(R.string.favorite)
                                    )
                                },
                                route = Route.Favorite
                            )
                        )
                    }

                    AdditionalNavigationItem.LISTS -> {
                        add(
                            NavigationItem(
                                label = R.string.my_lists,
                                unselectedIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_event_list_fill0_wght400),
                                        contentDescription = stringResource(R.string.my_lists)
                                    )
                                },
                                selectedIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_event_list_fill1_wght400),
                                        contentDescription = stringResource(R.string.my_lists)
                                    )
                                },
                                route = Route.MyLists
                            )
                        )
                    }

                    AdditionalNavigationItem.NONE -> {}
                }
            }
            add(
                NavigationItem(
                    label = if (userState().user?.sessionId.isNullOrEmpty()) R.string.profile else R.string.me,
                    unselectedIcon = {
                        ProfileIcon(isSelected = false, userState = userState)
                    },
                    selectedIcon = {
                        ProfileIcon(isSelected = true, userState = userState)
                    },
                    route = Route.Profile
                )
            )
        }
    }
}

@Composable
fun ProfileIcon(isSelected: Boolean, userState: () -> UserState) {
    if (userState().user?.sessionId.isNullOrEmpty()) {
        Icon(
            painter = painterResource(
                if (isSelected) R.drawable.icon_person_fill1_wght400 else R.drawable.icon_person_fill0_wght400
            ),
            contentDescription = stringResource(R.string.profile)
        )
    } else {
        val imageUrl = if (userState().user?.tmdbAvatarPath == null) {
            C.GRAVATAR_IMAGES_BASE_URL.format(userState().user?.gravatarAvatarPath)
        } else {
            C.TMDB_IMAGES_BASE_URL + C.PROFILE_W185 + userState().user?.tmdbAvatarPath
        }

        AsyncImage(
            model = imageUrl,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
            contentDescription = stringResource(R.string.me),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .then(
                    if (isSelected) Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ) else Modifier
                )
        )
    }
}