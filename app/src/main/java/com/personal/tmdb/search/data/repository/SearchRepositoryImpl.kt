package com.personal.tmdb.search.data.repository

import androidx.paging.Pager
import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.data.source.MediaPagingSource
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.core.domain.util.mediaPager
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
        return mediaPager {
            MediaPagingSource(
                loadPage = { page ->
                    safeApiCall {
                        tmdbApi.searchFor(searchType, query, includeAdult, language, page).toMediaResponseInfo()
                    }
                }
            )
        }
    }

    override suspend fun getTrendingList(
        timeWindow: TimeWindow,
        language: String?
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getTrending(timeWindow.name.lowercase(), language, 1).toMediaResponseInfo()
        }
    }

    override suspend fun getPopularPeopleList(
        mediaType: String,
        language: String?
    ): Pager<Int, MediaInfo> {
        return mediaPager {
            MediaPagingSource(
                loadPage = { page ->
                    safeApiCall {
                        tmdbApi.getPopular(mediaType, language, page).toMediaResponseInfo()
                    }
                },
                maxPage = 3
            )
        }
    }
}