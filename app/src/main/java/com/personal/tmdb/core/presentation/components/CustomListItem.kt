package com.personal.tmdb.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.dp

@Composable
fun CustomListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    headlineContent: @Composable () -> Unit,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    colors: CustomListItemColors = CustomListItemDefaults.colors(),
    shape: Shape = RectangleShape
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) colors.selectedContainerColor else colors.containerColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "Container Color"
    )

    Surface(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        color = containerColor,
        shape = shape
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent?.let {
                CompositionLocalProvider(
                    LocalContentColor provides colors.leadingIconColor,
                    content = it
                )
            }
            ProvideContentColorTextStyle(
                contentColor = colors.headlineColor,
                textStyle = MaterialTheme.typography.bodyLarge
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    headlineContent()
                }
            }
            trailingContent?.let {
                ProvideContentColorTextStyle(
                    contentColor = colors.trailingIconColor,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    content = it
                )
            }
        }
    }
}

/** Contains the default values used by list items. */
object CustomListItemDefaults {
    /**
     * Creates a [CustomListItemDefaults] that represents the default container and content colors used in a
     * [CustomListItem].
     */
    @Composable fun colors() = CustomListItemColors(
        containerColor = Color.Transparent,
        selectedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
        headlineColor = MaterialTheme.colorScheme.onSurface,
        leadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
        trailingIconColor = MaterialTheme.colorScheme.surfaceVariant
    )

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedContainerColor: Color = Color.Unspecified,
        headlineColor: Color = Color.Unspecified,
        leadingIconColor: Color = Color.Unspecified,
        trailingIconColor: Color = Color.Unspecified
    ): CustomListItemColors = colors().copy(
        containerColor = containerColor,
        selectedContainerColor = selectedContainerColor,
        headlineColor = headlineColor,
        leadingIconColor = leadingIconColor,
        trailingIconColor = trailingIconColor,
    )
}

/**
 * Represents the container and content colors used in a list item in different states.
 *
 * @param containerColor the container color of this list item when enabled.
 * @param selectedContainerColor the container color of this list item when selected.
 * @param headlineColor the headline text content color of this list item when enabled.
 * @param leadingIconColor the color of this list item's leading content when enabled.
 * @param trailingIconColor the color of this list item's trailing content when enabled.
 */
@Immutable
class CustomListItemColors(
    val containerColor: Color,
    val selectedContainerColor: Color,
    val headlineColor: Color,
    val leadingIconColor: Color,
    val trailingIconColor: Color
) {

    /**
     * Returns a copy of this [CustomListItemColors], optionally overriding some of the values. This uses the
     * Color.Unspecified to mean “use the value from the source”
     */
    fun copy(
        containerColor: Color = this.containerColor,
        selectedContainerColor: Color = this.selectedContainerColor,
        headlineColor: Color = this.headlineColor,
        leadingIconColor: Color = this.leadingIconColor,
        trailingIconColor: Color = this.trailingIconColor,
    ) = CustomListItemColors(
        containerColor.takeOrElse { this.containerColor },
        selectedContainerColor.takeOrElse { this.selectedContainerColor },
        headlineColor.takeOrElse { this.headlineColor },
        leadingIconColor.takeOrElse { this.leadingIconColor },
        trailingIconColor.takeOrElse { this.trailingIconColor },
    )
}