package com.personal.tmdb.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A Layout to correctly position and size [HorizontalSegmentedButton]s or [VerticalSegmentedButton]s in a Row.
 * It handles overlapping items so that strokes of the item are correctly on top of each other.
 * [SegmentedButtonsRow] is used when the selection only allows one value, for correct semantics.
 *
 * @param modifier the [Modifier] to be applied to this row.
 * @param space the dimension of the overlap between buttons. Should be equal to the stroke width
 *   used on the items.
 * @param containerColor the color to be applied to this row.
 * @param shape the [Shape] to be applied to this row.
 * @param content the content of this Segmented Button Row, typically a sequence of
 *   [VerticalSegmentedButton]s or [HorizontalSegmentedButton].
 */
@Composable
fun SegmentedButtonsRow(
    modifier: Modifier = Modifier,
    space: Dp = SegmentedButtonsSpace,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .selectableGroup()
            .clip(shape)
            .background(containerColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(space),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

/**
 * Selectable segmented buttons should be used for cases where the selection is mutually exclusive,
 * when only one button can be selected at a time.
 *
 * @param selected whether this button is selected or not
 * @param onClick callback to be invoked when the button is clicked. therefore the change of checked
 *   state in requested.
 * @param shape the shape for this button
 * @param modifier the [Modifier] to be applied to this button
 * @param enabled controls the enabled state of this button. When `false`, this component will not
 *   respond to user input, and it will appear visually disabled and disabled to accessibility
 *   services.
 * @param colors [SegmentedButtonColors] that will be used to resolve the colors used for this
 * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
 *   emitting [Interaction]s for this button. You can use this to change the button's appearance or
 *   preview the button in different states. Note that if `null` is provided, interactions will
 *   still happen internally.
 * @param icon the icon slot for this button, you can pass null in unchecked, in which case the
 *   content will displace to show the checked icon, or pass different icon lambdas for unchecked
 *   and checked in which case the icons will crossfade.
 * @param label content to be rendered inside this button
 */
@Composable
fun RowScope.VerticalSegmentedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    selected: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
    icon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val containerColor = containerColor(enabled, selected, colors)
    val contentColor = contentColor(enabled, selected, colors)
    val interactionCount = interactionSource.interactionCountAsState()

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Surface(
            modifier = modifier
                .weight(1f)
                .interactionZIndex(selected, interactionCount)
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight
                )
                .semantics { role = Role.RadioButton },
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            interactionSource = interactionSource
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelMedium
            ) {
                Column(
                    modifier = Modifier.padding(SegmentedButtonContentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ContentPadding)
                ) {
                    icon?.let { it() }
                    label()
                }
            }
        }
    }
}

/**
 * Selectable segmented buttons should be used for cases where the selection is mutually exclusive,
 * when only one button can be selected at a time.
 *
 * @param selected whether this button is selected or not
 * @param onClick callback to be invoked when the button is clicked. therefore the change of checked
 *   state in requested.
 * @param shape the shape for this button
 * @param modifier the [Modifier] to be applied to this button
 * @param enabled controls the enabled state of this button. When `false`, this component will not
 *   respond to user input, and it will appear visually disabled and disabled to accessibility
 *   services.
 * @param colors [SegmentedButtonColors] that will be used to resolve the colors used for this
 * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
 *   emitting [Interaction]s for this button. You can use this to change the button's appearance or
 *   preview the button in different states. Note that if `null` is provided, interactions will
 *   still happen internally.
 * @param icon the icon slot for this button, you can pass null in unchecked, in which case the
 *   content will displace to show the checked icon, or pass different icon lambdas for unchecked
 *   and checked in which case the icons will crossfade.
 * @param label content to be rendered inside this button
 */
@Composable
fun RowScope.HorizontalSegmentedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    selected: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
    icon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val containerColor = containerColor(enabled, selected, colors)
    val contentColor = contentColor(enabled, selected, colors)
    val interactionCount = interactionSource.interactionCountAsState()

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        Surface(
            modifier = modifier
                .weight(1f)
                .interactionZIndex(selected, interactionCount)
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight
                )
                .semantics { role = Role.RadioButton },
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            interactionSource = interactionSource
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.labelLarge
            ) {
                Row(
                    modifier = Modifier.padding(SegmentedButtonContentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ContentPadding, Alignment.CenterHorizontally)
                ) {
                    icon?.let { it() }
                    label()
                }
            }
        }
    }
}

/**
 * Represents the container color passed to the items
 *
 * @param enabled whether the [SegmentedButton] is enabled or not
 * @param active whether the [SegmentedButton] item is active or not
 */
@Stable
internal fun containerColor(enabled: Boolean, active: Boolean, colors: SegmentedButtonColors): Color {
    return when {
        enabled && active -> colors.activeContainerColor
        enabled && !active -> colors.inactiveContainerColor
        !enabled && active -> colors.disabledActiveContainerColor
        else -> colors.disabledInactiveContainerColor
    }
}

/**
 * Represents the content color passed to the items
 *
 * @param enabled whether the [SegmentedButton] is enabled or not
 * @param checked whether the [SegmentedButton] item is checked or not
 */
@Stable
internal fun contentColor(enabled: Boolean, checked: Boolean, colors: SegmentedButtonColors): Color {
    return when {
        enabled && checked -> colors.activeContentColor
        enabled && !checked -> colors.inactiveContentColor
        !enabled && checked -> colors.disabledActiveContentColor
        else -> colors.disabledInactiveContentColor
    }
}

@Composable
private fun InteractionSource.interactionCountAsState(): State<Int> {
    val interactionCount = remember { mutableIntStateOf(0) }
    LaunchedEffect(this) {
        this@interactionCountAsState.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press,
                is FocusInteraction.Focus -> {
                    interactionCount.intValue++
                }
                is PressInteraction.Release,
                is FocusInteraction.Unfocus,
                is PressInteraction.Cancel -> {
                    interactionCount.intValue--
                }
            }
        }
    }

    return interactionCount
}

private fun Modifier.interactionZIndex(checked: Boolean, interactionCount: State<Int>) =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            val zIndex = interactionCount.value + if (checked) CheckedZIndexFactor else 0f
            placeable.place(0, 0, zIndex)
        }
    }

private const val CheckedZIndexFactor = 5f

private val SegmentedButtonsSpace = 4.dp
private val ContentPadding = 8.dp
private val SegmentedButtonHorizontalPadding = 12.dp
private val SegmentedButtonVerticalPadding = 10.dp

/**
 * The default content padding used by [HorizontalSegmentedButton] and [VerticalSegmentedButton].
 */
private val SegmentedButtonContentPadding = PaddingValues(
    horizontal = SegmentedButtonHorizontalPadding,
    vertical = SegmentedButtonVerticalPadding
)