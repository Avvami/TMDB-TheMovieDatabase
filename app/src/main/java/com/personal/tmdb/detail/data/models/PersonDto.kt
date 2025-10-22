package com.personal.tmdb.detail.data.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonDto(
    @Json(name = "adult")
    val adult: Boolean?,
    @Json(name = "also_known_as")
    val alsoKnownAs: List<String>?,
    @Json(name = "biography")
    val biography: String?,
    @Json(name = "birthday")
    val birthday: String?,
    @Json(name = "combined_credits")
    val combinedCredits: CombinedCredits?,
    @Json(name = "deathday")
    val deathday: String?,
    @Json(name = "external_ids")
    val externalIds: ExternalIds?,
    @Json(name = "gender")
    val gender: Int,
    @Json(name = "homepage")
    val homepage: String?,
    @Json(name = "id")
    val id: Int,
    @Json(name = "images")
    val images: ImagesDto?,
    @Json(name = "imdb_id")
    val imdbId: String?,
    @Json(name = "known_for_department")
    val knownForDepartment: String?,
    @Json(name = "name")
    val name: String,
    @Json(name = "place_of_birth")
    val placeOfBirth: String?,
    @Json(name = "popularity")
    val popularity: Double?,
    @Json(name = "profile_path")
    val profilePath: String?
)