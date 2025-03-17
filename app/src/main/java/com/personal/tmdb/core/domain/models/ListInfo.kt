package com.personal.tmdb.core.domain.models

import androidx.compose.runtime.Stable
import java.time.LocalDateTime

@Stable
data class ListInfo(
    val adult: Boolean,
    val averageRating: Float?,
    val backdropPath: String?,
    val createdAt: LocalDateTime?,
    val description: String?,
    val featured: Int?,
    val id: Int,
    val iso31661: String?,
    val iso6391: String?,
    val name: String?,
    val numberOfItems: Int,
    val posterPath: String?,
    val public: Boolean,
    val revenue: Long?,
    val runtime: Int?,
    val sortBy: Int?,
    val updatedAt: LocalDateTime?,
    val loadingProgress: LoadingProgress = LoadingProgress.STILL
)