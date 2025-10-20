package com.personal.tmdb.core.domain.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.personal.tmdb.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.NavigableMap
import java.util.TreeMap

fun formatRuntime(minutes: Int): UiText {
    return when {
        minutes < 60 -> UiText.StringResource(R.string.runtime_min, minutes)
        minutes == 60 -> UiText.StringResource(R.string.runtime_h, minutes)
        minutes % 60 == 0 -> {
            val hours = minutes / 60
            UiText.StringResource(R.string.runtime_h, hours)
        }
        else -> {
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            UiText.StringResource(R.string.runtime_full, hours, remainingMinutes)
        }
    }
}

fun formatVoteAverage(voteAverage: Float): String {
    val roundedValue = (Math.round(voteAverage * 10) / 10.0).toFloat()
    return when {
        roundedValue == 0f -> "0"
        roundedValue % 1.0 == 0.0 -> roundedValue.toInt().toString()
        else -> "%.1f".format(voteAverage)
    }
}

fun formatVoteCount(voteCount: Int): String {
    return when (voteCount) {
        1 -> "$voteCount Rating"
        else -> "$voteCount Ratings"
    }
}

fun formatTvShowRuntime(numberOfSeasons: Int, numberOfEpisodes: Int): String {
    return when (numberOfSeasons) {
        1 -> formatEpisodesCount(numberOfEpisodes)
        else -> "$numberOfSeasons Seasons"
    }
}

fun formatEpisodesCount(numberOfEpisodes: Int): String {
    return when (numberOfEpisodes) {
        1 -> "$numberOfEpisodes Episode"
        else -> "$numberOfEpisodes Episodes"
    }
}

fun formatDate(localDate: LocalDate, pattern: String = "MMM d, yyyy"): String = localDate.format(DateTimeFormatter.ofPattern(pattern))

fun formatDateTime(localDateTime: LocalDateTime): UiText {
    val today = LocalDate.now()
    val formatter = when {
        localDateTime.year == today.year -> DateTimeFormatter.ofPattern("MMM d")
        else -> DateTimeFormatter.ofPattern("MMM d, yyyy")
    }
    return when {
        localDateTime.month == today.month && localDateTime.year == today.year && localDateTime.dayOfMonth == today.dayOfMonth -> UiText.StringResource(R.string.updated_today)
        localDateTime.month == today.month && localDateTime.year == today.year && today.dayOfMonth - localDateTime.dayOfMonth == 1 -> UiText.StringResource(R.string.updated_yesterday)
        else -> UiText.StringResource(R.string.updated_at, localDateTime.format(formatter))
    }
}

fun formatGender(genderCode: Int): UiText {
    return when (genderCode) {
        1 -> UiText.StringResource(R.string.female)
        2 -> UiText.StringResource(R.string.male)
        3 -> UiText.StringResource(R.string.non_binary)
        else -> UiText.StringResource(R.string.gender_not_specified)
    }
}

@Composable
fun formatPersonActing(
    primaryTextStyle: SpanStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface).toSpanStyle(),
    secondaryTextStyle: SpanStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surfaceVariant).toSpanStyle(),
    numberOfEpisodes: Int?,
    character: String?,
    job: String?
): AnnotatedString {
    val annotatedString by remember {
        derivedStateOf {
            buildAnnotatedString {
                numberOfEpisodes?.let {
                    withStyle(style = secondaryTextStyle) {
                        append("(${formatEpisodesCount(it)})")
                    }
                }
                withStyle(style = primaryTextStyle) {
                    if (numberOfEpisodes != null && (!character.isNullOrEmpty() || !job.isNullOrEmpty())) {
                        append(" ")
                    }
                    if (!character.isNullOrEmpty()) {
                        withStyle(style = secondaryTextStyle) {
                            append("as ")
                        }
                        append("$character")
                    }
                    if (!job.isNullOrEmpty()) {
                        withStyle(style = secondaryTextStyle) {
                            append("... ")
                        }
                        append("$job")
                    }
                }
            }
        }
    }
    return annotatedString
}

private val suffixes: NavigableMap<Long, String> = TreeMap<Long, String>().apply {
    put(1_000L, "K")
    put(1_000_000L, "M")
    put(1_000_000_000L, "B")
    put(1_000_000_000_000L, "T")
    put(1_000_000_000_000_000L, "P")
    put(1_000_000_000_000_000_000L, "E")
}

fun compactDecimalFormat(value: Long): String {
    if (value < 0) return "-" + compactDecimalFormat(-value)
    if (value < 1000) return value.toString()

    val (divideBy, suffix) = suffixes.floorEntry(value)?.toPair() ?: return value.toString()

    val truncated = value / (divideBy / 10)
    val hasDecimal = truncated < 100 && (truncated / 10.0) != (truncated / 10).toDouble()
    return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
}

fun formatNumberOfItems(numberOfItems: Int): String {
    return when (numberOfItems) {
        1 -> "$numberOfItems item"
        else -> "$numberOfItems items"
    }
}

fun formatListVisibility(isPublic: Boolean): UiText {
    return if (isPublic) UiText.StringResource(R.string.list_public) else UiText.StringResource(R.string.list_private)
}

fun capitalizeFirstLetter(input: String): String {
    return input.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

fun formatYearFilter(
    startYear: Int?, endYear: Int?
): UiText? {
    return when {
        startYear == null && endYear == null -> null
        startYear != null && endYear == null -> UiText.DynamicString(startYear.toString())
        else -> UiText.DynamicString("$startYear - $endYear")
    }
}