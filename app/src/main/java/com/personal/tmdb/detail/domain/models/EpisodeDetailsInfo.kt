package com.personal.tmdb.detail.domain.models

import com.personal.tmdb.detail.data.models.CastDto
import com.personal.tmdb.detail.data.models.Crew
import com.personal.tmdb.detail.data.models.Images
import com.personal.tmdb.detail.data.models.Translations
import java.time.LocalDate

data class EpisodeDetailsInfo(
    val airDate: LocalDate?,
    val crew: List<Crew>?,
    val episodeNumber: Int,
    val guestStars: List<CastDto>?,
    val id: Int,
    val images: Images?,
    val name: String?,
    val overview: String?,
    val productionCode: String?,
    val runtime: Int?,
    val seasonNumber: Int,
    val stillPath: String?,
    val translations: Translations?,
    val voteAverage: Float?,
    val voteCount: Int?
)
