package com.personal.tmdb.search.domain.repository

import androidx.paging.Pager
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.TimeWindow

interface SearchRepository {

    suspend fun searchFor(
        searchType: String,
        query: String,
        includeAdult: Boolean? = null,
        language: String? = null
    ): Pager<Int, MediaInfo>

    suspend fun getTrendingList(timeWindow: TimeWindow, language: String? = null): Result<MediaResponseInfo, DataError.Remote>

    suspend fun getPopularPeopleList(mediaType: String, language: String? = null): Pager<Int, MediaInfo>
}