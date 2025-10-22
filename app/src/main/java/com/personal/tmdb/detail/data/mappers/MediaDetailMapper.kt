package com.personal.tmdb.detail.data.mappers

import com.personal.tmdb.core.data.mappers.toMediaInfo
import com.personal.tmdb.core.domain.util.CountryCode
import com.personal.tmdb.core.domain.util.convertDateTimeToLocalDate
import com.personal.tmdb.core.domain.util.convertOffsetDateTimeToLocalDate
import com.personal.tmdb.detail.data.models.AccountStates
import com.personal.tmdb.detail.data.models.AvailableDto
import com.personal.tmdb.detail.data.models.BelongsToCollectionDto
import com.personal.tmdb.detail.data.models.CastDto
import com.personal.tmdb.detail.data.models.ContentRatingDto
import com.personal.tmdb.detail.data.models.Credits
import com.personal.tmdb.detail.data.models.EpisodeToAirDto
import com.personal.tmdb.detail.data.models.GenreDto
import com.personal.tmdb.detail.data.models.GenresDto
import com.personal.tmdb.detail.data.models.ImageDto
import com.personal.tmdb.detail.data.models.ImagesDto
import com.personal.tmdb.detail.data.models.MediaDetailDto
import com.personal.tmdb.detail.data.models.NetworkDto
import com.personal.tmdb.detail.data.models.ProductionCompanyDto
import com.personal.tmdb.detail.data.models.ProviderDto
import com.personal.tmdb.detail.data.models.ReleaseDatesResultsDto
import com.personal.tmdb.detail.data.models.VideoDto
import com.personal.tmdb.detail.data.models.WatchProvidersDto
import com.personal.tmdb.detail.domain.models.AccountState
import com.personal.tmdb.detail.domain.models.Available
import com.personal.tmdb.detail.domain.models.BelongsToCollection
import com.personal.tmdb.detail.domain.models.Cast
import com.personal.tmdb.detail.domain.models.ContentRating
import com.personal.tmdb.detail.domain.models.CreditsInfo
import com.personal.tmdb.detail.domain.models.EpisodeToAir
import com.personal.tmdb.detail.domain.models.Genre
import com.personal.tmdb.detail.domain.models.Genres
import com.personal.tmdb.detail.domain.models.Image
import com.personal.tmdb.detail.domain.models.Images
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.domain.models.Network
import com.personal.tmdb.detail.domain.models.ProductionCompany
import com.personal.tmdb.detail.domain.models.Provider
import com.personal.tmdb.detail.domain.models.ReleaseDate
import com.personal.tmdb.detail.domain.models.Video
import java.util.Locale

fun MediaDetailDto.toMediaDetail(): MediaDetail {
    val originalLanguage = try {
        Locale.forLanguageTag(originalLanguageCode ?: "").displayLanguage
    } catch (e: Exception) {
        null
    }
    val watchProviders = watchProviders?.toWatchProviders()
    println(aggregateCredits?.cast?.takeIf { it.isNotEmpty() }?.map { it.toCast() }
        ?: credits?.cast?.takeIf { it.isNotEmpty() }?.map { it.toCast() })
    return MediaDetail(
        accountStates = accountStates?.toAccountState(),
        backdropPath = backdropPath,
        belongsToCollection = belongsToCollection?.toBelongsToCollection(),
        budget = budget?.toLong()?.takeIf { it != 0L },
        cast = aggregateCredits?.cast?.takeIf { it.isNotEmpty() }?.map { it.toCast() }
            ?: credits?.cast?.takeIf { it.isNotEmpty() }?.map { it.toCast() },
        contentRatings = contentRatingsDto?.contentRatingsDto?.toContentRatings(),
        genres = genresDto?.map { it.toGenre() }?.takeIf { it.isNotEmpty() },
        id = id,
        inProduction = inProduction,
        images = images?.toImages(),
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
        releaseDates = releaseDatesResultsDto?.toReleaseDates(),
        revenue = revenue?.toLong()?.takeIf { it != 0L },
        reviews = reviews?.toReviewsResponse()?.results?.takeIf { it.isNotEmpty() },
        runtime = if (runtime == 0) null else runtime,
        seasons = seasons,
        status = status,
        tagline = if (tagline.isNullOrEmpty()) null else tagline,
        voteAverage = voteAverage?.toFloat()?.takeIf { it != 0f },
        voteCount = voteCount?.takeIf { it != 0 },
        videos = videosDto?.results?.toVideos(),
        watchProviders = watchProviders,
        watchCountries = watchProviders?.keys?.associate { countryCode ->
            val countryName = Locale.Builder().setRegion(countryCode).build().displayCountry
            countryCode to countryName
        }
    )
}

fun BelongsToCollectionDto.toBelongsToCollection(): BelongsToCollection {
    return BelongsToCollection(
        backdropPath = backdropPath,
        id = id,
        name = name?.takeIf { it.isNotEmpty() }
    )
}

fun CastDto.toCast(): Cast {
    return Cast(
        character = character?.takeIf { it.isNotEmpty() }
            ?: roles?.firstOrNull()?.character?.takeIf { it.isNotEmpty() },
        creditId = creditId,
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name?.takeIf { it.isNotEmpty() } ?: originalName?.takeIf { it.isNotEmpty() },
        popularity = popularity,
        profilePath = profilePath,
    )
}

fun List<ContentRatingDto>.toContentRatings(): List<ContentRating> {
    return this.map {
        ContentRating(
            iso31661 = it.iso31661,
            rating = it.rating?.takeIf { rating -> rating.isNotEmpty() }
        )
    }
}

fun GenresDto.toGenres(): Genres {
    return Genres(
        genres = genres?.map { it.toGenre() }?.takeIf { it.isNotEmpty() }
    )
}

fun GenreDto.toGenre(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun ImagesDto.toImages(): Images {
    return Images(
        profiles = profiles?.toImages(),
        stills = stills?.toImages(),
        backdrops = backdrops?.toImages(),
        posters = posters?.toImages(),
        logos = logos?.toImages()
    )
}

fun List<ImageDto>?.toImages(): List<Image>? {
    return this?.map {
        Image(
            aspectRatio = it.aspectRatio?.toFloat(),
            filePath = it.filePath,
            height = it.height,
            iso6391 = it.iso6391,
            width = it.width
        )
    }?.takeIf { it.isNotEmpty() }
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

fun ReleaseDatesResultsDto.toReleaseDates(): List<ReleaseDate>? {
    return this.releaseDatesResultsDto?.map {
        ReleaseDate(
            iso31661 = it.iso31661,
            certification = it.releaseDates?.firstOrNull { releaseDate ->
                !releaseDate.certification.isNullOrEmpty()
            }?.certification
        )
    }
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

fun WatchProvidersDto.toWatchProviders(): Map<CountryCode, Available>? {
    return results?.mapValues { (_, value) ->
        value.toAvailable()
    }?.takeIf { it.isNotEmpty() }
}

fun AvailableDto.toAvailable(): Available {
    return Available(
        link = link,
        ads = ads?.map { it.toProvider() }?.takeIf { it.isNotEmpty() },
        streaming = flatrate?.map { it.toProvider() }?.takeIf { it.isNotEmpty() },
        free = free?.map { it.toProvider() }?.takeIf { it.isNotEmpty() },
        buy = buy?.map { it.toProvider() }?.takeIf { it.isNotEmpty() },
        rent = rent?.map { it.toProvider() }?.takeIf { it.isNotEmpty() }
    )
}

fun ProviderDto.toProvider(): Provider {
    return Provider(
        displayPriority = displayPriority,
        logoPath = logoPath,
        providerId = providerId,
        providerName = providerName
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