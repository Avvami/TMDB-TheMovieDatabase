package com.personal.tmdb.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.search.data.source.SearchPagingSource
import com.personal.tmdb.search.data.source.SearchPopularPeoplePagingSource
import com.personal.tmdb.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): SearchRepository {
    override suspend fun searchFor(
        searchType: String,
        query: String,
        includeAdult: Boolean?,
        language: String?
    ): Pager<Int, MediaInfo> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                SearchPagingSource(
                    tmdbApi, searchType, query, includeAdult, language
                )
            }
        )
    }

    override suspend fun getTrendingList(
        timeWindow: TimeWindow,
        language: String?
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getTrendingList(timeWindow.name.lowercase(), language).toMediaResponseInfo()
        }
    }

    override suspend fun getPopularPeopleList(
        mediaType: String,
        language: String?
    ): Pager<Int, MediaInfo> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                SearchPopularPeoplePagingSource(tmdbApi, mediaType, language)
            }
        )
    }
}