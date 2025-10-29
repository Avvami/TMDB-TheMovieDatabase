package com.personal.tmdb.core.domain.models

import androidx.compose.runtime.Stable
import com.personal.tmdb.core.presentation.LoadState
import java.time.LocalDateTime

@Stable
data class MyList(
    val backdropPath: String?,
    val createdAt: LocalDateTime?,
    val description: String?,
    val id: Int,
    val iso31661: String?,
    val iso6391: String?,
    val name: String?,
    val numberOfItems: Int,
    val posterPath: String?,
    val public: Boolean,
    val sortBy: Int?,
    val updatedAt: LocalDateTime?,
    val loadState: LoadState = LoadState.NotLoading
)