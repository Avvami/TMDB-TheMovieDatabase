package com.personal.tmdb.core.domain.repository

import com.personal.tmdb.core.domain.models.CreateListRequest
import com.personal.tmdb.core.domain.models.ListDetailsInfo
import com.personal.tmdb.core.domain.models.ListsResponseInfo
import com.personal.tmdb.core.domain.models.MediaRequest
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.models.UpdateListMediaRequest
import com.personal.tmdb.core.domain.models.UpdateListDetailsRequest
import com.personal.tmdb.core.domain.models.User
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.EmptyResult
import com.personal.tmdb.core.domain.util.Result

interface UserRepository {

    suspend fun getUser(): User?

    suspend fun saveUser(user: User)

    suspend fun removeUser(user: User)

    suspend fun getWatchlist(
        accountObjectId: String,
        mediaType: String,
        sessionId: String,
        page: Int,
        language: String? = null
    ): Result<MediaResponseInfo, DataError.Remote>

    suspend fun updateWatchlistItem(accountId: Int, sessionId: String, mediaRequest: MediaRequest): EmptyResult<DataError.Remote>

    suspend fun getLists(accountObjectId: String, sessionId: String, page: Int): Result<ListsResponseInfo, DataError.Remote>

    suspend fun getListDetails(listId: Int, sessionId: String, page: Int, language: String? = null): Result<ListDetailsInfo, DataError.Remote>

    suspend fun updateListDetails(listId: Int, sessionId: String, updateListDetailsRequest: UpdateListDetailsRequest): EmptyResult<DataError.Remote>

    suspend fun deleteListItems(listId: Int, sessionId: String, updateListMediaRequest: UpdateListMediaRequest): EmptyResult<DataError.Remote>

    suspend fun deleteList(listId: Int, sessionId: String): EmptyResult<DataError.Remote>

    suspend fun createList(sessionId: String, createListRequest: CreateListRequest): EmptyResult<DataError.Remote>

    suspend fun addItemsToList(listId: Int, sessionId: String, updateListMediaRequest: UpdateListMediaRequest): EmptyResult<DataError.Remote>

    suspend fun getRecommendations(
        accountObjectId: String,
        mediaType: String,
        sessionId: String,
        page: Int,
        language: String? = null
    ): Result<MediaResponseInfo, DataError.Remote>

    suspend fun getFavorites(
        accountObjectId: String,
        mediaType: String,
        sessionId: String,
        page: Int,
        language: String? = null
    ): Result<MediaResponseInfo, DataError.Remote>

    suspend fun updateFavoriteItem(accountId: Int, sessionId: String, mediaRequest: MediaRequest): EmptyResult<DataError.Remote>
}