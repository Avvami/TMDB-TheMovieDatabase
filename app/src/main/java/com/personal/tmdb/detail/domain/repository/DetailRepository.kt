package com.personal.tmdb.detail.domain.repository

import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.detail.data.models.ImagesDto
import com.personal.tmdb.detail.domain.models.CollectionInfo
import com.personal.tmdb.detail.domain.models.CreditsInfo
import com.personal.tmdb.detail.domain.models.EpisodeDetailsInfo
import com.personal.tmdb.detail.domain.models.GenresInfo
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.domain.models.PersonInfo
import com.personal.tmdb.detail.domain.models.ReviewsResponse
import com.personal.tmdb.detail.domain.models.SeasonInfo
import com.personal.tmdb.detail.domain.models.AccountState

interface DetailRepository {

    suspend fun getMediaDetail(
        mediaType: String,
        mediaId: Int,
        sessionId: String?,
        language: String? = null,
        appendToResponse: String? = null,
        includeImageLanguage: String? = null
    ): Result<MediaDetail, DataError.Remote>

    suspend fun getAccountStates(mediaType: String, mediaId: Int, sessionId: String?): Result<AccountState, DataError.Remote>

    suspend fun getCollection(collectionId: Int, language: String? = null): Result<CollectionInfo, DataError.Remote>

    suspend fun getSeasonDetails(seriesId: Int, seasonNumber: Int, language: String? = null): Result<SeasonInfo, DataError.Remote>

    suspend fun getCast(mediaType: String, mediaId: Int, method: String, language: String? = null): Result<CreditsInfo, DataError.Remote>

    suspend fun getEpisodeCast(mediaId: Int, seasonNumber: Int, episodeNumber: Int, language: String? = null): Result<CreditsInfo, DataError.Remote>

    suspend fun getPerson(personId: Int, language: String? = null, appendToResponse: String? = null): Result<PersonInfo, DataError.Remote>

    suspend fun getEpisodeDetails(
        mediaId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: String? = null,
        appendToResponse: String? = null,
        includeImageLanguage: String? = null
    ): Result<EpisodeDetailsInfo, DataError.Remote>

    suspend fun getImages(path: String, language: String? = null, includeImageLanguage: String? = null): Result<ImagesDto, DataError.Remote>

    suspend fun getReviews(mediaType: String, mediaId: Int, page: Int, language: String?): Result<ReviewsResponse, DataError.Remote>

    suspend fun getGenres(mediaType: String, language: String?): Result<GenresInfo, DataError.Remote>
}