package com.personal.tmdb.discover.data.repository

import androidx.paging.Pager
import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.data.source.MediaPagingSource
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.defaultPager
import com.personal.tmdb.detail.data.mappers.toGenres
import com.personal.tmdb.detail.domain.models.Genres
import com.personal.tmdb.discover.data.mappers.toCountry
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): DiscoverRepository {

    override suspend fun getCountries(): Result<List<Country>, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getCountries().toCountry()
        }
    }

    override suspend fun getPopularPeople(
        language: String?
    ): Pager<Int, MediaInfo> {
        return defaultPager {
            MediaPagingSource(
                loadPage = { page ->
                    safeApiCall {
                        tmdbApi.getPopular(
                            mediaType = MediaType.PERSON.name.lowercase(),
                            language = language,
                            page = page
                        ).toMediaResponseInfo()
                    }
                }
            )
        }
    }

    override suspend fun getGenres(
        mediaType: String,
        language: String?
    ): Result<Genres, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getGenres(mediaType, language).toGenres()
        }
    }

    override suspend fun discoverMedia(
        mediaType: String,
        language: String?,
        includeAdult: Boolean,
        airDateYear: String,
        fromAirDate: String,
        toAirDate: String,
        sortBy: String,
        fromRating: Float,
        toRating: Float,
        minRatingCount: Float,
        withGenre: String,
        withOriginCountry: String,
        withOriginalLanguage: String,
        fromRuntime: Int,
        toRuntime: Int
    ):  Pager<Int, MediaInfo> {
        return defaultPager {
            MediaPagingSource(
                loadPage = { page ->
                    safeApiCall {
                        tmdbApi.discoverMedia(
                            mediaType = mediaType,
                            language = language,
                            page = page,
                            includeAdult = includeAdult,
                            airDateYear = airDateYear,
                            releaseDateYear = airDateYear,
                            fromAirDate = fromAirDate,
                            fromReleaseDate = fromAirDate,
                            toAirDate = toAirDate,
                            toReleaseDate = toAirDate,
                            sortBy = sortBy,
                            fromRating = fromRating,
                            toRating = toRating,
                            minRatingCount = minRatingCount,
                            withGenre = withGenre,
                            withOriginCountry = withOriginCountry,
                            withOriginalLanguage = withOriginalLanguage,
                            fromRuntime = fromRuntime,
                            toRuntime = toRuntime
                        ).toMediaResponseInfo()
                    }
                }
            )
        }
    }
}