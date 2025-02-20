package com.personal.tmdb.core.data.mappers

import com.personal.tmdb.core.data.models.ListDetailsDto
import com.personal.tmdb.core.data.models.ListResult
import com.personal.tmdb.core.data.models.ListsResponseDto
import com.personal.tmdb.core.domain.models.ListDetailsInfo
import com.personal.tmdb.core.domain.models.ListInfo
import com.personal.tmdb.core.domain.models.ListsResponseInfo
import com.personal.tmdb.core.domain.util.convertStringToDateTime
import com.personal.tmdb.core.domain.util.toBoolean

fun ListsResponseDto.toListsResponseInfo(): ListsResponseInfo {
    return ListsResponseInfo(
        page = page,
        results = results.map { it.toListInfo() },
        totalPages = totalPages,
        totalResults = totalResults
    )
}

fun ListResult.toListInfo(): ListInfo {
    return ListInfo(
        adult = adult.toBoolean(),
        averageRating = averageRating?.toFloat(),
        backdropPath = backdropPath,
        createdAt = convertStringToDateTime(createdAt),
        description = description?.takeIf { it.isNotEmpty() },
        featured = featured,
        id = id,
        iso31661 = iso31661,
        iso6391 = iso6391,
        name = name?.takeIf { it.isNotEmpty() },
        numberOfItems = numberOfItems ?: 0,
        posterPath = posterPath,
        public = public.toBoolean(),
        revenue = revenue,
        runtime = runtime?.toIntOrNull()?.takeIf { it != 0 },
        sortBy = sortBy,
        updatedAt = convertStringToDateTime(updatedAt)
    )
}

fun ListDetailsDto.toListDetailsInfo(): ListDetailsInfo {
    return ListDetailsInfo(
        averageRating = averageRating?.toFloat(),
        backdropPath = backdropPath,
        createdBy = createdBy,
        description = description?.takeIf { it.isNotEmpty() },
        id = id,
        iso31661 = iso31661,
        iso6391 = iso6391,
        itemCount = itemCount,
        name = name?.takeIf { it.isNotEmpty() },
        page = page,
        posterPath = posterPath,
        public = public,
        results = results.map { it.toMediaInfo() },
        revenue = revenue,
        runtime = runtime?.takeIf { it != 0 },
        sortBy = sortBy,
        totalPages = totalPages,
        totalResults = totalResults
    )
}