package com.personal.tmdb.core.data.remote

import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.squareup.moshi.JsonDataException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): Result<T, DataError.Remote> {
    return try {
        val result = apiCall()
        Result.Success(result)
    } catch (e: SocketTimeoutException) {
        Result.Error(DataError.Remote.RequestTimeout)
    } catch (e: UnknownHostException) {
        Result.Error(DataError.Remote.NoInternet)
    } catch (e: JsonDataException) {
        Result.Error(DataError.Remote.Serialization)
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        val statusMessage = extractStatusMessage(errorBody)

        when (e.code()) {
            406 -> Result.Error(DataError.Remote.InvalidHeader)
            408 -> Result.Error(DataError.Remote.RequestTimeout)
            429 -> Result.Error(DataError.Remote.TooManyRequests)
            500 -> Result.Error(DataError.Remote.InternalError)
            501 -> Result.Error(DataError.Remote.InvalidService)
            502 -> Result.Error(DataError.Remote.BackedConnection)
            503 -> Result.Error(DataError.Remote.ApiMaintenance)
            504 -> Result.Error(DataError.Remote.BackendTimeout)
            in 505..599 -> Result.Error(DataError.Remote.Server)
            else -> Result.Error(
                if (!statusMessage.isNullOrEmpty()) DataError.Remote.Custom(statusMessage) else DataError.Remote.Unknown
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(DataError.Remote.Unknown)
    }
}

fun extractStatusMessage(errorBody: String?): String? {
    return try {
        errorBody?.let {
            val json = JSONObject(errorBody)
            json.optString("status_message")
        }
    } catch (e: Exception) {
        null
    }
}