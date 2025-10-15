package com.personal.tmdb.detail.data.mappers

import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.domain.util.convertStringToDate
import com.personal.tmdb.core.domain.util.convertISO8601toLocalDate
import com.personal.tmdb.detail.data.models.AccountStates
import com.personal.tmdb.detail.data.models.Credits
import com.personal.tmdb.detail.data.models.EpisodeToAir
import com.personal.tmdb.detail.data.models.MediaDetailDto
import com.personal.tmdb.detail.data.models.VideoDto
import com.personal.tmdb.detail.domain.models.AccountState
import com.personal.tmdb.detail.domain.models.CreditsInfo
import com.personal.tmdb.detail.domain.models.EpisodeToAirInfo
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.domain.models.Video
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun MediaDetailDto.toMediaDetailInfo(): MediaDetail {
    return MediaDetail(
        accountStates = accountStates?.toAccountState(),
        aggregateCredits = aggregateCredits,
        backdropPath = backdropPath,
        belongsToCollection = belongsToCollection,
        cast = aggregateCredits?.cast?.take(15) ?: credits?.cast?.take(15),
        contentRatings = contentRatings,
        createdBy = createdBy?.take(2),
        credits = credits,
        genres = genres,
        id = id,
        images = images,
        lastEpisodeToAir = lastEpisodeToAir?.toEpisodeToAirInfo(),
        name = title ?: name,
        networks = networks,
        nextEpisodeToAir = nextEpisodeToAir?.toEpisodeToAirInfo(),
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        originCountry = originCountry,
        originalLanguage = try { Locale.forLanguageTag(originalLanguage ?: "").displayLanguage } catch (e: Exception) { null },
        originalName = originalName,
        overview = if (overview.isNullOrEmpty()) null else overview,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        recommendations = recommendations?.toMediaResponseInfo(),
        releaseDate = convertStringToDate(firstAirDate?.takeIf { it.isNotBlank() } ?: releaseDate?.takeIf { it.isNotBlank() }),
        releaseDates = releaseDates,
        reviews = reviews?.toReviewsResponseInfo(),
        runtime = if (runtime == 0) null else runtime,
        seasons = seasons,
        similar = similar?.toMediaResponseInfo(),
        status = status,
        tagline = if (tagline.isNullOrEmpty()) null else tagline,
        voteAverage = voteAverage?.toFloat(),
        voteCount = voteCount,
        videos = videosDto?.results?.takeIf { it.isNotEmpty() }?.map { it.toVideo() }
            ?.filter { it.official },
        watchProviders = watchProviders?.watchProvidersResults?.mapKeys { (countryCode, _) -> Locale("", countryCode).displayCountry }
    )
}

fun VideoDto.toVideo(): Video {
    return Video(
        id = id,
        key = key,
        name = name?.takeIf { it.isNotEmpty() },
        official = official,
        publishedAt = convertISO8601toLocalDate(publishedAt),
        type = type
    )
}

fun AccountStates.toAccountState(): AccountState {
    return AccountState(favorite, rated, watchlist)
}

fun Credits.toCreditsInfo(): CreditsInfo {
    return CreditsInfo(
        cast = cast,
        crew = crew?.groupBy { it.department },
        guestStars = guestStars
    )
}

fun EpisodeToAir.toEpisodeToAirInfo(): EpisodeToAirInfo {
    val airDate = try {
        airDate?.let { string ->
            LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    } catch (e: Exception) {
        null
    }
    return EpisodeToAirInfo(
        airDate = airDate,
        episodeNumber = episodeNumber,
        episodeType = episodeType,
        id = id,
        name = if (name.isNullOrEmpty()) null else name,
        overview = if (overview.isNullOrEmpty()) null else overview,
        productionCode = productionCode,
        runtime = if (runtime == 0) null else runtime,
        seasonNumber = seasonNumber,
        showId = showId,
        stillPath = stillPath,
        voteAverage = voteAverage?.toFloat()
    )
}