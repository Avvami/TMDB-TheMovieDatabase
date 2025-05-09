package com.personal.tmdb.home.domain.repository

import androidx.paging.Pager
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.TimeWindow

interface HomeRepository {

    suspend fun getTrending(timeWindow: TimeWindow, language: String? = null): Pager<Int, MediaInfo>

    suspend fun getTrendingPage(timeWindow: TimeWindow, language: String? = null): Result<MediaResponseInfo, DataError.Remote>

    suspend fun getPopular(mediaType: MediaType, language: String? = null): Pager<Int, MediaInfo>

    suspend fun getUpcomingMovies(language: String? = null, region: String): Pager<Int, MediaInfo>
}