package com.personal.tmdb.detail.domain.models

data class Cast(
    val character: String?,
    val creditId: String?,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String?,
    val name: String?,
    val popularity: Double?,
    val profilePath: String?
)
