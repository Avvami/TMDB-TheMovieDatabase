package com.personal.tmdb.detail.domain.models

import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.CountryCode
import com.personal.tmdb.core.domain.util.CountryName
import com.personal.tmdb.detail.data.models.ContentRatings
import com.personal.tmdb.detail.data.models.CreatedBy
import com.personal.tmdb.detail.data.models.Credits
import com.personal.tmdb.detail.data.models.Genre
import com.personal.tmdb.detail.data.models.ReleaseDates
import com.personal.tmdb.detail.data.models.Season
import java.time.LocalDate

data class MediaDetail(
    val accountStates: AccountState?,
    val cast: List<Cast>?,
    val backdropPath: String?,
    val belongsToCollection: BelongsToCollection?,
    val budget: Long?,
    val contentRatings: ContentRatings?,
    val createdBy: List<CreatedBy>?,
    val credits: Credits?,
    val genres: List<Genre>?,
    val id: Int,
    val images: Images?,
    val lastEpisodeToAir: EpisodeToAir?,
    val name: String?,
    val networks: List<Network>?,
    val nextEpisodeToAir: EpisodeToAir?,
    val numberOfEpisodes: Int?,
    val numberOfSeasons: Int?,
    val originCountry: List<String>?,
    val originalLanguage: String?,
    val originalName: String?,
    val overview: String?,
    val posterPath: String?,
    val productionCompanies: List<ProductionCompany>?,
    val recommendations: List<MediaInfo>?,
    val releaseDate: LocalDate?,
    val releaseDates: ReleaseDates?,
    val revenue: Long?,
    val reviews: List<Review>?,
    val runtime: Int?,
    val seasons: List<Season>?,
    val status: String?,
    val tagline: String?,
    val voteAverage: Float?,
    val voteCount: Int?,
    val videos: List<Video>?,
    val watchProviders: Map<CountryCode, Available>?,
    val watchCountries: Map<CountryCode, CountryName>?,
)