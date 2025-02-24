package com.personal.tmdb.core.domain.repository

import com.personal.tmdb.core.domain.models.ListDetailsInfo
import com.personal.tmdb.core.domain.models.ListsResponseInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.models.RemoveMediaRequest
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

    suspend fun getLists(accountObjectId: String, sessionId: String, page: Int): Result<ListsResponseInfo, DataError.Remote>

    suspend fun getListDetails(listId: Int, sessionId: String, page: Int, language: String? = null): Result<ListDetailsInfo, DataError.Remote>

    suspend fun deleteListItems(listId: Int, sessionId: String, removeMediaRequest: RemoveMediaRequest): EmptyResult<DataError.Remote>

    suspend fun getRecommendations(
        accountObjectId: String,
        mediaType: String,
        sessionId: String,
        page: Int,
        language: String? = null
    ): Result<MediaResponseInfo, DataError.Remote>
}