package com.personal.tmdb.detail.data.mappers

import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.convertMediaType
import com.personal.tmdb.core.domain.util.convertDateTimeToLocalDate
import com.personal.tmdb.detail.data.models.CombinedCastCrew
import com.personal.tmdb.detail.data.models.CombinedCredits
import com.personal.tmdb.detail.domain.models.CombinedCastCrewInfo
import com.personal.tmdb.detail.domain.models.CombinedCreditsInfo
import com.personal.tmdb.detail.domain.models.Jobs

fun CombinedCredits.toCombinedCreditsInfo(): CombinedCreditsInfo {
    return CombinedCreditsInfo(
        credits = ((cast ?: emptyList()) + (crew?: emptyList())).toGroupedCastCrewInfo(),
        castMediaInfo = cast
            ?.groupBy { it.id }
            ?.map { (_, idGroup) ->
                idGroup.reduce { acc, castCrew ->
                    acc.copy(job = listOf(acc.character, castCrew.character).joinToString(", ").trimEnd(',', ' '))
                }
            }
            ?.map { it.toMediaInfo() }
            ?.take(15),
        crewMediaInfo = crew
            ?.groupBy { it.department }
            ?.map { (_, depGroup) ->
                depGroup
                    .groupBy { it.id }
                    .map { (_, idGroup) ->
                        idGroup.reduce { acc, castCrew ->
                            acc.copy(job = listOf(acc.job, castCrew.job).joinToString(", ").trimEnd(',', ' '))
                        }
                    }
            }
            ?.maxByOrNull { it.size }
            ?.map { it.toMediaInfo() }
            ?.take(15)
    )
}

fun List<CombinedCastCrew>?.toGroupedCastCrewInfo(): Map<String?, Map<Int, List<CombinedCastCrewInfo>>?>? {
    return this
        ?.groupBy { it.department }
        ?.mapValues { (_, items) ->
            items
                .groupBy { it.id }
                .mapNotNull { (_, groupItems) ->
                    val jobs = groupItems.map { Jobs(it.character, it.episodeCount, it.job) }
                    val firstItem = groupItems.firstOrNull()
                    firstItem?.let { item ->
                        CombinedCastCrewInfo(
                            backdropPath = item.backdropPath,
                            department = item.department,
                            id = item.id,
                            jobs = jobs,
                            mediaType = convertMediaType(item.mediaType),
                            name = item.title ?: item.name,
                            overview = if (item.overview.isNullOrEmpty()) null else item.overview,
                            posterPath = item.posterPath,
                            releaseDate = convertDateTimeToLocalDate(
                                value = item.firstAirDate?.takeIf { it.isNotBlank() } ?: item.releaseDate?.takeIf { it.isNotBlank() }
                            )
                        )
                    }
                }
                .groupBy { it.releaseDate?.year ?: 50000 }
                .toSortedMap(compareByDescending { it })
                .mapValues { entry -> entry.value.sortedByDescending { it.releaseDate } }
        }
}

fun CombinedCastCrew.toMediaInfo(): MediaInfo {
    return MediaInfo(
        backdropPath = backdropPath,
        id = id,
        mediaType = convertMediaType(mediaType),
        name = title ?: name,
        originalLanguage = originalLanguage,
        overview = if (overview.isNullOrEmpty()) null else overview,
        posterPath = posterPath,
        releaseDate = convertDateTimeToLocalDate(
            value = firstAirDate?.takeIf { it.isNotBlank() } ?: releaseDate?.takeIf { it.isNotBlank() }
        ),
        voteAverage = voteAverage?.toFloat()
    )
}