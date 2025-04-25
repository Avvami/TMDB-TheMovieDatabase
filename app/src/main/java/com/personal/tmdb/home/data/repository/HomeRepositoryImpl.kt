package com.personal.tmdb.home.data.repository

import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.detail.data.mappers.toGenresInfo
import com.personal.tmdb.detail.domain.models.GenresInfo
import com.personal.tmdb.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): HomeRepository {
    override suspend fun getTrendingList(timeWindow: TimeWindow, language: String?): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getTrendingList(timeWindow.name.lowercase(), language).toMediaResponseInfo()
        }
    }

    override suspend fun getNowPlaying(language: String?): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getNowPlaying(language).toMediaResponseInfo()
        }
    }

    override suspend fun getPopularList(
        mediaType: String,
        language: String?,
        page: Int
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getPopularList(mediaType, language, page).toMediaResponseInfo()
        }
    }

    override suspend fun discoverMedia(
        mediaType: String,
        language: String?,
        page: Int
    ): Result<MediaResponseInfo, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun getGenres(
        mediaType: String,
        language: String?
    ): Result<GenresInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getGenres(mediaType, language).toGenresInfo()
        }
    }
}