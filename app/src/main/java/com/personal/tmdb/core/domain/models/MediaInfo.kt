package com.personal.tmdb.core.domain.models

import androidx.compose.runtime.Stable
import com.personal.tmdb.core.domain.util.MediaType
import java.time.LocalDate
import java.util.UUID

@Stable
data class MediaInfo(
    val uuid: UUID = UUID.randomUUID(),
    val backdropPath: String?,
    val id: Int,
    val knownFor: List<MediaInfo>? = null,
    val mediaType: MediaType?,
    val name: String?,
    val originalLanguage: String?,
    val overview: String?,
    val posterPath: String?,
    val releaseDate: LocalDate?,
    val voteAverage: Float?
)