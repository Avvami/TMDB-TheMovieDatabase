package com.personal.tmdb.core.presentation.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personal.tmdb.core.presentation.motion.materialSharedAxisXIn
import com.personal.tmdb.core.presentation.motion.materialSharedAxisXOut

const val initialOffset = 0.10f

inline fun <reified T: Any> NavGraphBuilder.animatedComposable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable<T>(
    deepLinks = deepLinks,
    enterTransition = {
        materialSharedAxisXIn(initialOffsetX = { (it * initialOffset).toInt() })
    },
    exitTransition = {
        materialSharedAxisXOut(targetOffsetX = { -(it * initialOffset).toInt() })
    },
    popEnterTransition = {
        materialSharedAxisXIn(initialOffsetX = { -(it * initialOffset).toInt() })
    },
    popExitTransition = {
        materialSharedAxisXOut(targetOffsetX = { (it * initialOffset).toInt() })
    },
    content = content
)

inline fun <reified T: Any> NavGraphBuilder.staticComposable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable<T>(
    deepLinks = deepLinks,
    enterTransition = { EnterTransition.None },
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { ExitTransition.None },
    content = content
)