package com.personal.tmdb.core.domain.util

import android.app.Activity
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.personal.tmdb.detail.domain.models.Image
import com.personal.tmdb.detail.domain.models.Images
import kotlin.math.absoluteValue

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

fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

fun PagerState.indicatorOffsetForPage(page: Int) =
    1f - offsetForPage(page).coerceIn(-1f, 1f).absoluteValue

fun Activity.showSystemBars() {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
}

fun Activity.hideSystemBars() {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
}
