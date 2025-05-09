package com.personal.tmdb.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val NavigationBarHeight: Dp = 48.dp
private val NavigationBarWidth: Dp = 640.dp
private val NavigationBarItemWidth: Dp = 64.dp
private const val ItemAnimationDurationMillis: Int = 100

@Composable
fun CustomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = containerColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets),
            contentAlignment = Alignment.Center
        ) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.TopCenter),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
            )
            Row(
                modifier = Modifier
                    .widthIn(max = NavigationBarWidth)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = NavigationBarHeight)
                    .selectableGroup(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Composable
fun RowScope.CustomNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(),
    interactionSource: MutableInteractionSource? = null
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val styledIcon =
        @Composable {
            val iconColor by
            animateColorAsState(
                targetValue = iconColor(selected = selected, enabled = enabled, colors = colors),
                animationSpec = tween(ItemAnimationDurationMillis),
                label = ""
            )
            // If there's a label, don't have a11y services repeat the icon description.
            val clearSemantics = label != null && (alwaysShowLabel || selected)
            Box(modifier = if (clearSemantics) Modifier.clearAndSetSemantics {} else Modifier) {
                CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
            }
        }

    val styledLabel: @Composable (() -> Unit)? =
        label?.let {
            @Composable {
                val style = MaterialTheme.typography.labelSmall
                val textColor by
                animateColorAsState(
                    targetValue = textColor(selected = selected, enabled = enabled, colors = colors),
                    animationSpec = tween(ItemAnimationDurationMillis),
                    label = ""
                )
                ProvideContentColorTextStyle(
                    contentColor = textColor,
                    textStyle = style,
                    content = label
                )
            }
        }

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null
            )
            .defaultMinSize(minHeight = NavigationBarHeight, minWidth = NavigationBarItemWidth)
            .weight(1f),
        contentAlignment = Alignment.Center,
        propagateMinConstraints = true
    ) {
        val animationProgress: State<Float> =
            animateFloatAsState(
                targetValue = if (selected) 1f else 0f,
                animationSpec = tween(ItemAnimationDurationMillis),
                label = ""
            )

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            styledIcon()
            if (styledLabel != null) {
                Box(
                    modifier = Modifier.graphicsLayer { alpha = if (alwaysShowLabel) 1f else animationProgress.value }
                ) {
                    styledLabel()
                }
            }
        }
    }
}

@Stable
internal fun iconColor(selected: Boolean, enabled: Boolean, colors: NavigationBarItemColors): Color =
    when {
        !enabled -> colors.disabledIconColor
        selected -> colors.selectedIconColor
        else -> colors.unselectedIconColor
    }

@Stable
internal fun textColor(selected: Boolean, enabled: Boolean, colors: NavigationBarItemColors): Color =
    when {
        !enabled -> colors.disabledTextColor
        selected -> colors.selectedTextColor
        else -> colors.unselectedTextColor
    }

/**
 * ProvideContentColorTextStyle
 *
 * A convenience method to provide values to both LocalContentColor and LocalTextStyle in one call.
 * This is less expensive than nesting calls to CompositionLocalProvider.
 *
 * Text styles will be merged with the current value of LocalTextStyle.
 */
@Composable
internal fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}