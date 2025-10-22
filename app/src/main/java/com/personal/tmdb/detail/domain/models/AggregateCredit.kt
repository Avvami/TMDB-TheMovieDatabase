package com.personal.tmdb.detail.domain.models

import com.personal.tmdb.detail.data.models.Role

data class AggregateCredit(
    val adult: Boolean,
    val character: String?,
    val creditId: String?,
    val department: String?,
    val gender: Int,
    val id: Int,
    val job: String?,
    val knownForDepartment: String?,
    val name: String,
    val order: Int?,
    val originalName: String?,
    val popularity: Double?,
    val profilePath: String?,
    val roles: List<Role>?
)
