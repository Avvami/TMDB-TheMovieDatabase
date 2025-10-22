package com.personal.tmdb.core.domain.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.personal.tmdb.detail.domain.models.Image
import com.personal.tmdb.detail.domain.models.Images

/**
 * Attempts to find a logo [Image] with the desired language preference.
 *
 * @param preferred ISO 639-1 code for the preferred language.
 * @param fallback ISO 639-1 code as a secondary option if preferred isn't found.
 * @return The [Image] of the matched logo, or the first available one, or null if none exist.
 */
fun Images.findLogoImageWithLanguage(preferred: String?, fallback: String?): Image? {
    val logoList = logos?.takeIf { it.isNotEmpty() } ?: return null
    return logoList.firstOrNull { it.iso6391 == preferred }
        ?: logoList.firstOrNull { it.iso6391 == fallback }
        ?: logoList.firstOrNull()
}

/**
 * Converts a [Dp] value to a [Float] pixel value using the current screen density.
 *
 * Example usage:
 * ```
 * val someDp: Dp = 16.dp
 * val pixels: Float = someDp.toPxFloat()
 * ```
 *
 * @return the equivalent pixel value as a [Float] corresponding to the [Dp] value on the current screen density
 */
@Composable
fun Dp.toPxFloat(): Float {
    val density = LocalDensity.current
    return with(density) { this@toPxFloat.toPx() }
}
