package com.personal.tmdb.core.domain.util

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.unit.Dp

@Stable
fun customRipple(
    color: ColorProducer,
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    rippleAlpha: RippleAlpha = CustomAlpha
): IndicationNodeFactory {
    return CustomNodeFactory(bounded, radius, color, rippleAlpha)
}

@Stable
fun customRipple(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified,
    rippleAlpha: RippleAlpha = CustomAlpha
): IndicationNodeFactory {
    return CustomNodeFactory(bounded, radius, color, rippleAlpha)
}

private class CustomNodeFactory private constructor(
    private val bounded: Boolean,
    private val radius: Dp,
    private val colorProducer: ColorProducer?,
    private val color: Color,
    private val rippleAlpha: RippleAlpha,
) : IndicationNodeFactory {
    constructor(
        bounded: Boolean,
        radius: Dp,
        colorProducer: ColorProducer,
        rippleAlpha: RippleAlpha
    ) : this(bounded, radius, colorProducer, Color.Unspecified, rippleAlpha )
    constructor(
        bounded: Boolean,
        radius: Dp,
        color: Color,
        rippleAlpha: RippleAlpha
    ) : this(bounded, radius, null, color, rippleAlpha)
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        val colorProducer = colorProducer ?: ColorProducer { color }
        return DelegatingRippleNode(
            interactionSource,
            bounded,
            radius,
            colorProducer,
            rippleAlpha
        )
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomNodeFactory) return false
        if (bounded != other.bounded) return false
        if (radius != other.radius) return false
        if (colorProducer != other.colorProducer) return false
        return color == other.color
    }
    override fun hashCode(): Int {
        var result = bounded.hashCode()
        result = 31 * result + radius.hashCode()
        result = 31 * result + colorProducer.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}

private class DelegatingRippleNode(
    private val interactionSource: InteractionSource,
    private val bounded: Boolean,
    private val radius: Dp,
    private val color: ColorProducer,
    private val rippleAlpha: RippleAlpha,
) : DelegatingNode(), CompositionLocalConsumerModifierNode {
    override fun onAttach() {
        val calculateColor = ColorProducer {
            val userDefinedColor = color()
            if (userDefinedColor.isSpecified) {
                userDefinedColor
            } else {
                currentValueOf(LocalContentColor)
            }
        }
        val calculateRippleAlpha = { rippleAlpha }
        delegate(
            createRippleModifierNode(
                interactionSource,
                bounded,
                radius,
                calculateColor,
                calculateRippleAlpha
            )
        )
    }
}
private val CustomAlpha: RippleAlpha = RippleAlpha(
    pressedAlpha = 1f,
    focusedAlpha = 1f,
    draggedAlpha = 1f,
    hoveredAlpha = 1f
)