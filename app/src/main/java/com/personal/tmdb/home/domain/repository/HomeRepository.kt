package com.personal.tmdb.home.domain.repository

import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.TimeWindow

interface HomeRepository {
    suspend fun getTrendingList(timeWindow: TimeWindow, language: String? = null): Result<MediaResponseInfo, DataError.Remote>

    suspend fun getNowPlaying(language: String? = null): Result<MediaResponseInfo, DataError.Remote>
}