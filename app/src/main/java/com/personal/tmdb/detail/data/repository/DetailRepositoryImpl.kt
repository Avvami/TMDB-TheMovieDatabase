package com.personal.tmdb.detail.data.repository

import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.detail.data.mappers.toCollectionInfo
import com.personal.tmdb.detail.data.mappers.toCreditsInfo
import com.personal.tmdb.detail.data.mappers.toEpisodeDetailsInfo
import com.personal.tmdb.detail.data.mappers.toGenresInfo
import com.personal.tmdb.detail.data.mappers.toMediaDetailInfo
import com.personal.tmdb.detail.data.mappers.toPersonInfo
import com.personal.tmdb.detail.data.mappers.toReviewsResponseInfo
import com.personal.tmdb.detail.data.mappers.toSeasonInfo
import com.personal.tmdb.detail.data.models.Images
import com.personal.tmdb.detail.domain.models.CollectionInfo
import com.personal.tmdb.detail.domain.models.CreditsInfo
import com.personal.tmdb.detail.domain.models.EpisodeDetailsInfo
import com.personal.tmdb.detail.domain.models.GenresInfo
import com.personal.tmdb.detail.domain.models.MediaDetailInfo
import com.personal.tmdb.detail.domain.models.PersonInfo
import com.personal.tmdb.detail.domain.models.ReviewsResponseInfo
import com.personal.tmdb.detail.domain.models.SeasonInfo
import com.personal.tmdb.detail.domain.repository.DetailRepository
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): DetailRepository {
    override suspend fun getMediaDetail(
        mediaType: String,
        mediaId: Int,
        sessionId: String?,
        language: String?,
        appendToResponse: String?,
        includeImageLanguage: String?
    ): Result<MediaDetailInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getMovieTvDetail(
                mediaType = mediaType,
                mediaId = mediaId,
                sessionId = sessionId,
                language = language,
                appendToResponse = appendToResponse,
                includeImageLanguage = includeImageLanguage
            ).toMediaDetailInfo()
        }
    }

    override suspend fun getCollection(
        collectionId: Int,
        language: String?
    ): Result<CollectionInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getCollection(
                collectionId = collectionId,
                language = language
            ).toCollectionInfo()
        }
    }

    override suspend fun getSeasonDetails(
        seriesId: Int,
        seasonNumber: Int,
        language: String?
    ): Result<SeasonInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getSeasonDetails(seriesId, seasonNumber, language).toSeasonInfo()
        }
    }

    override suspend fun getCast(
        mediaType: String,
        mediaId: Int,
        method: String,
        language: String?
    ): Result<CreditsInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getCredits(mediaType, mediaId, method, language).toCreditsInfo()
        }
    }

    override suspend fun getEpisodeCast(
        mediaId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: String?
    ): Result<CreditsInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getEpisodeCredits(mediaId, seasonNumber, episodeNumber, language).toCreditsInfo()
        }
    }

    override suspend fun getPerson(
        personId: Int,
        language: String?,
        appendToResponse: String?
    ): Result<PersonInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getPerson(personId, language, appendToResponse).toPersonInfo()
        }
    }

    override suspend fun getEpisodeDetails(
        mediaId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: String?,
        appendToResponse: String?,
        includeImageLanguage: String?
    ): Result<EpisodeDetailsInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getEpisodeDetails(
                mediaId,
                seasonNumber,
                episodeNumber,
                language,
                appendToResponse,
                includeImageLanguage
            ).toEpisodeDetailsInfo()
        }
    }

    override suspend fun getImages(
        path: String,
        language: String?,
        includeImageLanguage: String?
    ): Result<Images, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getImages(path, language, includeImageLanguage)
        }
    }

    override suspend fun getReviews(
        mediaType: String,
        mediaId: Int,
        page: Int,
        language: String?
    ): Result<ReviewsResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getReviews(mediaType, mediaId, page, language).toReviewsResponseInfo()
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
}