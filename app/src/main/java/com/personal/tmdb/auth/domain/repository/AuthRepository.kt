package com.personal.tmdb.auth.domain.repository

import com.personal.tmdb.auth.data.models.AccessTokenBody
import com.personal.tmdb.auth.data.models.AccessTokenDto
import com.personal.tmdb.auth.data.models.RedirectToBody
import com.personal.tmdb.auth.data.models.RequestTokenBody
import com.personal.tmdb.auth.data.models.RequestTokenDto
import com.personal.tmdb.auth.data.models.SessionDto
import com.personal.tmdb.auth.domain.models.UserInfo
import com.personal.tmdb.core.domain.models.LogoutRequestBody
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.EmptyResult
import com.personal.tmdb.core.domain.util.Result

interface AuthRepository {

    suspend fun createRequestToken(redirectToBody: RedirectToBody): Result<RequestTokenDto, DataError.Remote>

    suspend fun createAccessToken(requestTokenBody: RequestTokenBody): Result<AccessTokenDto, DataError.Remote>

    suspend fun createSession(accessTokenBody: AccessTokenBody): Result<SessionDto, DataError.Remote>

    suspend fun getUserDetails(sessionId: String): Result<UserInfo, DataError.Remote>

    suspend fun logout(request: LogoutRequestBody): EmptyResult<DataError.Remote>
}