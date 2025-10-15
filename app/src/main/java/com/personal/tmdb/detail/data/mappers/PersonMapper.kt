package com.personal.tmdb.detail.data.mappers

import com.personal.tmdb.core.domain.util.convertDateTimeToLocalDate
import com.personal.tmdb.detail.data.models.PersonDto
import com.personal.tmdb.detail.domain.models.PersonInfo

fun PersonDto.toPersonInfo(): PersonInfo {
    return PersonInfo(
        alsoKnownAs = alsoKnownAs,
        biography = if (biography.isNullOrEmpty()) null else biography,
        birthday = convertDateTimeToLocalDate(birthday),
        combinedCreditsInfo = combinedCredits?.toCombinedCreditsInfo(),
        deathday = convertDateTimeToLocalDate(deathday),
        externalIds = externalIds,
        gender = gender,
        homepage = homepage,
        id = id,
        images = images,
        imdbId = imdbId,
        knownForDepartment = knownForDepartment,
        name = name,
        placeOfBirth = placeOfBirth,
        popularity = popularity,
        profilePath = profilePath
    )
}