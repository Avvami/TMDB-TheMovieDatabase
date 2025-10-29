package com.personal.tmdb.core.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyListDto(
    @Json(name = "account_object_id")
    val accountObjectId: String?,
    @Json(name = "adult")
    val adult: Int?,
    @Json(name = "average_rating")
    val averageRating: Double?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "featured")
    val featured: Int?,
    @Json(name = "id")
    val id: Int,
    @Json(name = "iso_3166_1")
    val iso31661: String?,
    @Json(name = "iso_639_1")
    val iso6391: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "number_of_items")
    val numberOfItems: Int?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "public")
    val public: Int?,
    @Json(name = "revenue")
    val revenue: Long?,
    @Json(name = "runtime")
    val runtime: String?,
    @Json(name = "sort_by")
    val sortBy: Int?,
    @Json(name = "updated_at")
    val updatedAt: String?
)
