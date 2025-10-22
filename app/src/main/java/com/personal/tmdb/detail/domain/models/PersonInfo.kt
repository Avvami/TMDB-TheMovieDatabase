package com.personal.tmdb.detail.domain.models

import com.personal.tmdb.detail.data.models.ExternalIds
import com.personal.tmdb.detail.data.models.ImagesDto
import java.time.LocalDate

data class PersonInfo(
    val alsoKnownAs: List<String>?,
    val biography: String?,
    val birthday: LocalDate?,
    val combinedCreditsInfo: CombinedCreditsInfo?,
    val deathday: LocalDate?,
    val externalIds: ExternalIds?,
    val gender: Int,
    val homepage: String?,
    val id: Int,
    val images: ImagesDto?,
    val imdbId: String?,
    val knownForDepartment: String?,
    val name: String,
    val placeOfBirth: String?,
    val popularity: Double?,
    val profilePath: String?
)
