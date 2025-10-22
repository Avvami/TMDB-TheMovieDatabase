package com.personal.tmdb.detail.domain.models

import com.personal.tmdb.detail.data.models.CastDto
import com.personal.tmdb.detail.data.models.Crew

data class CreditsInfo(
    val cast: List<CastDto>?,
    val crew: Map<String?, List<Crew>?>?,
    val guestStars: List<CastDto>?,
)
