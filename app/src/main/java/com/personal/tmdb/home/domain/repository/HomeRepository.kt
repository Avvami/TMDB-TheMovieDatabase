package com.personal.tmdb.home.domain.repository

import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.detail.domain.models.GenresInfo

interface HomeRepository {
    suspend fun getTrendingList(timeWindow: TimeWindow, language: String? = null): Result<MediaResponseInfo, DataError.Remote>

    suspend fun getNowPlaying(language: String? = null): Result<MediaResponseInfo, DataError.Remote>

    suspend fun getPopularList(mediaType: String, language: String? = null, page: Int): Result<MediaResponseInfo, DataError.Remote>

    suspend fun discoverMedia(
        mediaType: String,
        language: String? = null,
        page: Int
    ): Result<MediaResponseInfo, DataError.Remote>

    suspend fun getGenres(mediaType: String, language: String?): Result<GenresInfo, DataError.Remote>
}