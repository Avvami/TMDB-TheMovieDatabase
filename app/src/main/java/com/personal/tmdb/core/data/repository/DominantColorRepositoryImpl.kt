package com.personal.tmdb.core.data.repository

import androidx.collection.LruCache
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.size.Scale
import coil3.toBitmap
import com.personal.tmdb.core.domain.repository.DominantColorRepository
import com.personal.tmdb.core.domain.util.DominantColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DominantColorRepositoryImpl @Inject constructor(
    private val imageRequestBuilder: ImageRequest.Builder,
    private val imageLoaderBuilder: ImageLoader.Builder
): DominantColorRepository {

    /**
     * A function which calculates and caches the result of calculated dominant color
     * from images.
     *
     * @param imageUrl The url of target image.
     * @param cacheSize The size of the [LruCache] used to store recent results. Pass `0` to
     * disable the cache.
     */
    override suspend fun calculateDominantColor(
        imageUrl: String,
        cacheSize: Int
    ): DominantColors? {
        val cache = when {
            cacheSize > 0 -> LruCache<String, DominantColors>(cacheSize)
            else -> null
        }

        val cached = cache?.get(imageUrl)
        if (cached != null) {
            // If we already have the result cached, return early now...
            return cached
        }

        // Otherwise we calculate the swatches in the image, and return the first valid color
        return calculateVibrantInImage(imageUrl)
            ?.let { swatch ->
                DominantColors(
                    color = Color(swatch.rgb),
                    onColor = Color(swatch.bodyTextColor).copy(alpha = 1f)
                )
            }
            // Cache the resulting [DominantColors]
            ?.also { result -> cache?.put(imageUrl, result) }
    }

    /**
     * Fetches the given [imageUrl] with [Coil], then uses [Palette] to calculate the dominant color.
     */
    private suspend fun calculateVibrantInImage(imageUrl: String): Palette.Swatch? {
        val imageRequest = imageRequestBuilder
            .data(imageUrl)
            // We scale the image to cover 128px x 128px (i.e. min dimension == 128px)
            .size(128).scale(Scale.FILL)
            // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
            .allowHardware(false)
            .build()

        val imageLoader = imageLoaderBuilder.build()
        val bitmap = when (val result = imageLoader.execute(imageRequest)) {
            is SuccessResult -> result.image.toBitmap()
            else -> null
        }
        return bitmap?.let {
            withContext(Dispatchers.Default) {
                val palette = Palette.Builder(bitmap)
                    // Disable any bitmap resizing in Palette. We've already loaded an appropriately
                    // sized bitmap through Coil
                    .resizeBitmapArea(0)
                    // Clear any built-in filters. We want the unfiltered dominant color
                    .clearFilters()
                    // We reduce the maximum color count down to 8
                    .maximumColorCount(8)
                    .generate()

                palette.vibrantSwatch ?: palette.dominantSwatch ?: palette.mutedSwatch
            }
        }
    }
}