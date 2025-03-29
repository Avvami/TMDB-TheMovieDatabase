package com.personal.tmdb.core.domain.repository

import com.personal.tmdb.core.domain.util.DominantColors

interface DominantColorRepository {
    suspend fun calculateDominantColor(imageUrl: String, cacheSize: Int = 12): DominantColors?
}