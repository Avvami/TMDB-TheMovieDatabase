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

/**
 * Parses a string in ISO 8601 offset datetime format (e.g. "2019-10-16T01:47:56.000Z")
 * and returns the corresponding [LocalDate].
 * Returns null if parsing fails or input is null.
 *
 * @param value The ISO 8601 datetime string with offset.
 * @return [LocalDate] extracted from the datetime or null if invalid input.
 */
fun convertOffsetDateTimeToLocalDate(value: String?): LocalDate? {
    return try {
        value?.let { string ->
            OffsetDateTime.parse(string).toLocalDate()
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Parses a string in ISO 8601 local date format (e.g. "2024-02-01")
 * and returns the corresponding [LocalDate].
 * Returns null if parsing fails or input is null.
 *
 * @param value The ISO 8601 date string without time.
 * @return [LocalDate] representing the parsed date or null if invalid input.
 */
fun convertDateTimeToLocalDate(value: String?): LocalDate? {
    return try {
        value?.let { string ->
            LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Parses a date-time string with pattern "yyyy-MM-dd HH:mm:ss z" including a timezone (e.g. "2025-02-03 05:18:34 UTC"),
 * converts it to the system default timezone, and returns the LocalDateTime.
 * Returns null if parsing fails or input is null.
 *
 * @param value The custom formatted date-time string with timezone abbreviation.
 * @return LocalDateTime converted to system default timezone or null if invalid input.
 */
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