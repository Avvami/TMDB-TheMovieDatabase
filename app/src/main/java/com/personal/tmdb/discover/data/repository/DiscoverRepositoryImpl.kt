package com.personal.tmdb.discover.data.repository

import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.detail.data.mappers.toGenresInfo
import com.personal.tmdb.detail.domain.models.GenresInfo
import com.personal.tmdb.discover.data.mappers.toCountry
import com.personal.tmdb.discover.data.mappers.toLanguage
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.models.Language
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

    override suspend fun getLanguages(): Result<List<Language>, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getLanguages().toLanguage()
        }
    }

    override suspend fun getPopularPeople(
        language: String?,
        page: Int
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getPopularList(MediaType.PERSON.name.lowercase(), language, page).toMediaResponseInfo()
        }
    }

    override suspend fun getGenres(
        mediaType: String,
        language: String?
    ): Result<GenresInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getGenres(mediaType, language).toGenresInfo()
        }
    }

    override suspend fun discoverMedia(
        mediaType: String,
        language: String?,
        page: Int,
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
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
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
}