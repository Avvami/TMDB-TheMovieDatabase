package com.personal.tmdb.auth.data.repository

import com.personal.tmdb.auth.data.mappers.toUserInfo
import com.personal.tmdb.auth.data.models.AccessTokenBody
import com.personal.tmdb.auth.data.models.AccessTokenDto
import com.personal.tmdb.auth.data.models.RedirectToBody
import com.personal.tmdb.auth.data.models.RequestTokenBody
import com.personal.tmdb.auth.data.models.RequestTokenDto
import com.personal.tmdb.auth.data.models.SessionDto
import com.personal.tmdb.auth.domain.models.UserInfo
import com.personal.tmdb.auth.domain.repository.AuthRepository
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.models.LogoutRequestBody
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.EmptyResult
import com.personal.tmdb.core.domain.util.Result
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): AuthRepository {
    override suspend fun createRequestToken(redirectToBody: RedirectToBody): Result<RequestTokenDto, DataError.Remote> {
        return safeApiCall {
            tmdbApi.createRequestToken(redirectTo = redirectToBody)
        }
    }

    override suspend fun createAccessToken(requestTokenBody: RequestTokenBody): Result<AccessTokenDto, DataError.Remote> {
        return safeApiCall {
            tmdbApi.createAccessToken(requestToken = requestTokenBody)
        }
    }

    override suspend fun createSession(accessTokenBody: AccessTokenBody): Result<SessionDto, DataError.Remote> {
        return safeApiCall {
            tmdbApi.createSession(accessToken = accessTokenBody)
        }
    }

    override suspend fun getUserDetails(sessionId: String): Result<UserInfo, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getUserDetails(sessionId).toUserInfo()
        }
    }

    override suspend fun logout(request: LogoutRequestBody): EmptyResult<DataError.Remote> {
        return safeApiCall {
            tmdbApi.removeSessionId(request)
            tmdbApi.removeAccessToken(request)
        }
    }
}