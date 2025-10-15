package com.personal.tmdb.detail.domain.models

import java.time.LocalDate

data class EpisodeToAir(
    val airDate: LocalDate?,
    val episodeNumber: Int,
    val episodeType: String?,
    val id: Int,
    val name: String?,
    val overview: String?,
    val productionCode: String?,
    val runtime: Int?,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String?,
    val voteAverage: Float?
)
