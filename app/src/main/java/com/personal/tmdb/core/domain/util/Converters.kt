package com.personal.tmdb.core.domain.util

import androidx.compose.ui.graphics.Color
import com.personal.tmdb.R
import com.personal.tmdb.ui.theme.ratingHigh
import com.personal.tmdb.ui.theme.ratingLow
import com.personal.tmdb.ui.theme.ratingMid
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun convertISO8601toLocalDate(value: String?): LocalDate? {
    return try {
        value?.let { string ->
            OffsetDateTime.parse(string).toLocalDate()
        }
    } catch (e: Exception) {
        null
    }
}

fun convertStringToDate(dateString: String?): LocalDate? {
    return try {
        dateString?.let { string ->
            LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    } catch (e: Exception) {
        null
    }
}

fun convertStringToDateTime(dateString: String?): LocalDateTime? {
    return try {
        dateString?.let { string ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
            val zonedDateTime = ZonedDateTime.parse(string, formatter)
            zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        }
    } catch (e: Exception) {
        null
    }
}

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.Local.DiskFull -> UiText.StringResource(R.string.error_disk_full)
        DataError.Local.Unknown -> UiText.StringResource(R.string.error_unknown)
        DataError.Remote.RequestTimeout -> UiText.StringResource(R.string.error_request_timeout)
        DataError.Remote.TooManyRequests -> UiText.StringResource(R.string.error_too_many_requests)
        DataError.Remote.NoInternet -> UiText.StringResource(R.string.error_no_internet)
        DataError.Remote.InvalidService -> UiText.StringResource(R.string.error_invalid_service)
        DataError.Remote.InternalError -> UiText.StringResource(R.string.error_internal_error)
        DataError.Remote.InvalidHeader -> UiText.StringResource(R.string.error_invalid_header)
        DataError.Remote.ApiMaintenance -> UiText.StringResource(R.string.error_api_maintenance)
        DataError.Remote.BackedConnection -> UiText.StringResource(R.string.error_backend_connection)
        DataError.Remote.BackendTimeout -> UiText.StringResource(R.string.error_backend_timeout)
        DataError.Remote.Server -> UiText.StringResource(R.string.error_unknown)
        DataError.Remote.Serialization -> UiText.StringResource(R.string.error_serialization)
        DataError.Remote.Unknown -> UiText.StringResource(R.string.error_unknown)
        is DataError.Remote.Custom -> UiText.DynamicString(statusMessage)
    }
}

fun Int?.toBoolean(): Boolean = this == 1

fun getColorForVoteAverage(voteAverage: Float): Color {
    return when {
        voteAverage < 5f -> ratingLow
        voteAverage < 7f -> ratingMid
        else -> ratingHigh
    }
}