package com.personal.tmdb

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.personal.tmdb.core.domain.util.CustomDns
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
class TmdbApplication: Application(), SingletonImageLoader.Factory {

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        val okHttpClient = OkHttpClient.Builder()
            .dns(CustomDns())
            .build()

        return ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir)
                    .maxSizeBytes(50 * 1024 * 1024) // 50MB
                    .build()
            }
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = okHttpClient))
                add(SvgDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }
}