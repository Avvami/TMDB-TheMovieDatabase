package com.personal.tmdb.detail.data.mappers

import com.personal.tmdb.core.data.mappers.toMediaInfo
import com.personal.tmdb.core.domain.util.convertDateTimeToLocalDate
import com.personal.tmdb.core.domain.util.convertOffsetDateTimeToLocalDate
import com.personal.tmdb.detail.data.models.AccountStates
import com.personal.tmdb.detail.data.models.Credits
import com.personal.tmdb.detail.data.models.EpisodeToAirDto
import com.personal.tmdb.detail.data.models.MediaDetailDto
import com.personal.tmdb.detail.data.models.NetworkDto
import com.personal.tmdb.detail.data.models.ProductionCompanyDto
import com.personal.tmdb.detail.data.models.VideoDto
import com.personal.tmdb.detail.domain.models.AccountState
import com.personal.tmdb.detail.domain.models.CreditsInfo
import com.personal.tmdb.detail.domain.models.EpisodeToAir
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.domain.models.Network
import com.personal.tmdb.detail.domain.models.ProductionCompany
import com.personal.tmdb.detail.domain.models.Video
import java.util.Locale

fun MediaDetailDto.toMediaDetail(): MediaDetail {
    val originalLanguage = try { Locale.forLanguageTag(originalLanguageCode ?: "").displayLanguage } catch (e: Exception) { null }
    return MediaDetail(
        accountStates = accountStates?.toAccountState(),
        aggregateCredits = aggregateCredits,
        backdropPath = backdropPath,
        belongsToCollection = belongsToCollection,
        budget = budget?.toLong(),
        cast = aggregateCredits?.cast?.take(15) ?: credits?.cast?.take(15),
        contentRatings = contentRatings,
        createdBy = createdBy?.take(2),
        credits = credits,
        genres = genres?.takeIf { it.isNotEmpty() },
        id = id,
        images = images,
        lastEpisodeToAir = lastEpisodeToAir?.toEpisodeToAir(),
        name = title ?: name,
        networks = networksDto?.toNetworks(),
        nextEpisodeToAir = nextEpisodeToAir?.toEpisodeToAir(),
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        originCountry = originCountry,
        originalLanguage = originalLanguage,
        originalName = originalName?.takeIf { it.isNotEmpty() },
        overview = if (overview.isNullOrEmpty()) null else overview,
        posterPath = posterPath,
        productionCompanies = productionCompaniesDto?.toProductionCompanies(),
        recommendations = recommendations?.results?.map { it.toMediaInfo() }
            ?.takeIf { it.isNotEmpty() },
        releaseDate = convertDateTimeToLocalDate(
            firstAirDate?.takeIf { it.isNotBlank() } ?: releaseDate?.takeIf { it.isNotBlank() }
        ),
        releaseDates = releaseDates,
        revenue = revenue?.toLong(),
        reviews = reviews?.toReviewsResponseInfo(),
        runtime = if (runtime == 0) null else runtime,
        seasons = seasons,
        status = status,
        tagline = if (tagline.isNullOrEmpty()) null else tagline,
        voteAverage = voteAverage?.toFloat()?.takeIf { it != 0f },
        voteCount = voteCount?.takeIf { it != 0 },
        videos = videosDto?.results?.toVideos(),
        watchProviders = watchProviders?.watchProvidersResults?.mapKeys { (countryCode, _) -> Locale("", countryCode).displayCountry }
    )
}

fun List<NetworkDto>?.toNetworks(): List<Network>? {
    return this?.map {
        Network(
            id = it.id,
            logoPath = it.logoPath,
            name = it.name ?: "",
            originCountry = it.originCountry
        )
    }?.filter { it.name.isNotEmpty() }?.takeIf { it.isNotEmpty() }
}

fun List<ProductionCompanyDto>?.toProductionCompanies(): List<ProductionCompany>? {
    return this?.map {
        ProductionCompany(
            id = it.id,
            logoPath = it.logoPath,
            name = it.name ?: "",
            originCountry = it.originCountry
        )
    }?.filter { it.name.isNotEmpty() }?.takeIf { it.isNotEmpty() }
}

fun List<VideoDto>?.toVideos(): List<Video>? {
    return this?.map { videoDto ->
        Video(
            id = videoDto.id,
            key = videoDto.key,
            name = videoDto.name?.takeIf { it.isNotEmpty() },
            official = videoDto.official,
            publishedAt = convertOffsetDateTimeToLocalDate(videoDto.publishedAt),
            type = videoDto.type
        )
    }?.filter { it.official }?.takeIf { it.isNotEmpty() }
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