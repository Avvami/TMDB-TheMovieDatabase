package com.personal.tmdb.detail.data.mappers

import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.domain.util.convertDateTimeToLocalDate
import com.personal.tmdb.core.domain.util.convertOffsetDateTimeToLocalDate
import com.personal.tmdb.detail.data.models.AccountStates
import com.personal.tmdb.detail.data.models.Credits
import com.personal.tmdb.detail.data.models.EpisodeToAirDto
import com.personal.tmdb.detail.data.models.MediaDetailDto
import com.personal.tmdb.detail.data.models.VideoDto
import com.personal.tmdb.detail.domain.models.AccountState
import com.personal.tmdb.detail.domain.models.CreditsInfo
import com.personal.tmdb.detail.domain.models.EpisodeToAir
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.domain.models.Video
import java.util.Locale

fun MediaDetailDto.toMediaDetail(): MediaDetail {
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
        lastEpisodeToAir = lastEpisodeToAir?.toEpisodeToAir(),
        name = title ?: name,
        networks = networks,
        nextEpisodeToAir = nextEpisodeToAir?.toEpisodeToAir(),
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        originCountry = originCountry,
        originalLanguage = try { Locale.forLanguageTag(originalLanguage ?: "").displayLanguage } catch (e: Exception) { null },
        originalName = originalName,
        overview = if (overview.isNullOrEmpty()) null else overview,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        recommendations = recommendations?.toMediaResponseInfo(),
        releaseDate = convertDateTimeToLocalDate(firstAirDate?.takeIf { it.isNotBlank() } ?: releaseDate?.takeIf { it.isNotBlank() }),
        releaseDates = releaseDates,
        reviews = reviews?.toReviewsResponseInfo(),
        runtime = if (runtime == 0) null else runtime,
        seasons = seasons,
        similar = similar?.toMediaResponseInfo(),
        status = status,
        tagline = if (tagline.isNullOrEmpty()) null else tagline,
        voteAverage = voteAverage?.toFloat()?.takeIf { it != 0f },
        voteCount = voteCount?.takeIf { it != 0 },
        videos = videosDto?.results?.map { it.toVideo() }?.filter { it.official }
            ?.takeIf { it.isNotEmpty() },
        watchProviders = watchProviders?.watchProvidersResults?.mapKeys { (countryCode, _) -> Locale("", countryCode).displayCountry }
    )
}

fun VideoDto.toVideo(): Video {
    return Video(
        id = id,
        key = key,
        name = name?.takeIf { it.isNotEmpty() },
        official = official,
        publishedAt = convertOffsetDateTimeToLocalDate(publishedAt),
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

fun EpisodeToAirDto.toEpisodeToAir(): EpisodeToAir {
    return EpisodeToAir(
        airDate = convertDateTimeToLocalDate(airDate),
        episodeNumber = episodeNumber,
        episodeType = episodeType,
        id = id,
        name = name?.takeIf { it.isNotEmpty() },
        overview = overview?.takeIf { it.isNotEmpty() },
        productionCode = productionCode,
        runtime = runtime?.takeIf { it != 0 },
        seasonNumber = seasonNumber,
        showId = showId,
        stillPath = stillPath,
        voteAverage = voteAverage?.toFloat()
    )
}