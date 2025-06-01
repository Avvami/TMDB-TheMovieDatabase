package com.personal.tmdb.home.data.repository

import androidx.paging.Pager
import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.data.source.MediaPagingSource
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.SortType
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.core.domain.util.mediaPager
import com.personal.tmdb.core.domain.util.sortTypeToRequestString
import com.personal.tmdb.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): HomeRepository {

    override suspend fun getTrending(timeWindow: TimeWindow, language: String?): Pager<Int, MediaInfo> {
        return mediaPager {
            MediaPagingSource(
                loadPage = { page ->
                    safeApiCall {
                        tmdbApi.getTrending(timeWindow.name.lowercase(), language, page).toMediaResponseInfo()
                    }
                },
                maxPage = 5
            )
        }
    }

    override suspend fun getTrendingPage(
        timeWindow: TimeWindow,
        language: String?
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getTrending(timeWindow.name.lowercase(), language, 1).toMediaResponseInfo()
        }
    }

    override suspend fun getPopular(mediaType: MediaType, language: String?): Pager<Int, MediaInfo> {
        return mediaPager {
            MediaPagingSource(
                loadPage = { page ->
                    safeApiCall {
                        tmdbApi.discoverMedia(
                            mediaType = mediaType.name.lowercase(),
                            language = language,
                            page = page,
                            includeAdult = false,
                            airDateYear = "",
                            releaseDateYear = "",
                            fromAirDate = "",
                            fromReleaseDate = "",
                            toAirDate = "",
                            toReleaseDate = "",
                            sortBy = sortTypeToRequestString(SortType.POPULAR, mediaType),
                            fromRating = 0f,
                            toRating = 10f,
                            minRatingCount = 200f,
                            withGenre = "",
                            withOriginCountry = "",
                            withOriginalLanguage = "",
                            fromRuntime = 0,
                            toRuntime = 360
                        ).toMediaResponseInfo()
                    }
                },
                maxPage = 5
            )
        }
    }

    override suspend fun getUpcomingMovies(
        language: String?,
        region: String
    ): Pager<Int, MediaInfo> {
        return mediaPager {
            MediaPagingSource(
                loadPage = { page ->
                    safeApiCall {
                        tmdbApi.getUpcomingMovies(language, page, region).toMediaResponseInfo()
                    }
                },
                maxPage = 5
            )
        }
    }
}