package com.personal.tmdb.core.data.repository

import com.personal.tmdb.core.data.local.UserDao
import com.personal.tmdb.core.data.mappers.toListDetailsInfo
import com.personal.tmdb.core.data.mappers.toListsResponseInfo
import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.mappers.toUser
import com.personal.tmdb.core.data.mappers.toUserEntity
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.models.CreateListRequest
import com.personal.tmdb.core.domain.models.ListDetailsInfo
import com.personal.tmdb.core.domain.models.ListsResponseInfo
import com.personal.tmdb.core.domain.models.MediaRequest
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.models.UpdateListMediaRequest
import com.personal.tmdb.core.domain.models.UpdateListDetailsRequest
import com.personal.tmdb.core.domain.models.User
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.EmptyResult
import com.personal.tmdb.core.domain.util.Result
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val dao: UserDao
): UserRepository {

    override suspend fun getUser(): User? = dao.getUser()?.toUser()

    override suspend fun saveUser(user: User) = dao.saveUser(user.toUserEntity())

    override suspend fun removeUser(user: User) = dao.removeUser(user.toUserEntity())

    override suspend fun getWatchlist(
        accountObjectId: String,
        mediaType: String,
        sessionId: String,
        page: Int,
        language: String?
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getWatchlist(
                accountObjectId = accountObjectId,
                mediaType = mediaType,
                sessionId = sessionId,
                page = page,
                language = language
            ).toMediaResponseInfo()
        }
    }

    override suspend fun updateWatchlistItem(
        accountId: Int,
        sessionId: String,
        mediaRequest: MediaRequest
    ): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.updateWatchlistItem(accountId, sessionId, mediaRequest)
        }
    }

    override suspend fun getLists(
        accountObjectId: String,
        sessionId: String,
        page: Int
    ): Result<ListsResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getLists(accountObjectId = accountObjectId, sessionId = sessionId, page = page).toListsResponseInfo()
        }
    }

    override suspend fun getListDetails(
        listId: Int,
        sessionId: String,
        page: Int,
        language: String?
    ): Result<ListDetailsInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getListDetails(listId, sessionId, page, language).toListDetailsInfo()
        }
    }

    override suspend fun updateListDetails(
        listId: Int,
        sessionId: String,
        updateListDetailsRequest: UpdateListDetailsRequest
    ): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.updateListDetails(listId, sessionId, updateListDetailsRequest)
        }
    }

    override suspend fun deleteListItems(
        listId: Int,
        sessionId: String,
        updateListMediaRequest: UpdateListMediaRequest
    ): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.deleteListItems(listId, sessionId, updateListMediaRequest)
        }
    }

    override suspend fun deleteList(listId: Int, sessionId: String): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.deleteList(listId, sessionId)
        }
    }

    override suspend fun createList(
        sessionId: String,
        createListRequest: CreateListRequest
    ): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.createList(sessionId, createListRequest)
        }
    }

    override suspend fun addItemsToList(
        listId: Int,
        sessionId: String,
        updateListMediaRequest: UpdateListMediaRequest
    ): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.addItemsToList(listId, sessionId, updateListMediaRequest)
        }
    }

    override suspend fun getRecommendations(
        accountObjectId: String,
        mediaType: String,
        sessionId: String,
        page: Int,
        language: String?
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getRecommendations(
                accountObjectId = accountObjectId,
                mediaType = mediaType,
                sessionId = sessionId,
                page = page,
                language = language
            ).toMediaResponseInfo()
        }
    }

    override suspend fun getFavorites(
        accountObjectId: String,
        mediaType: String,
        sessionId: String,
        page: Int,
        language: String?
    ): Result<MediaResponseInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getFavorites(
                accountObjectId = accountObjectId,
                mediaType = mediaType,
                sessionId = sessionId,
                page = page,
                language = language
            ).toMediaResponseInfo()
        }
    }

    override suspend fun updateFavoriteItem(
        accountId: Int,
        sessionId: String,
        mediaRequest: MediaRequest
    ): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.updateFavoriteItem(accountId, sessionId, mediaRequest)
        }
    }
}