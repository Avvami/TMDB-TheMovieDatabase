package com.personal.tmdb.core.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListDetailsDto(
    @Json(name = "average_rating")
    val averageRating: Double?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "created_by")
    val createdBy: CreatedBy?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: Int,
    @Json(name = "iso_3166_1")
    val iso31661: String?,
    @Json(name = "iso_639_1")
    val iso6391: String?,
    @Json(name = "item_count")
    val itemCount: Int,
    @Json(name = "name")
    val name: String?,
    @Json(name = "page")
    val page: Int,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "public")
    val public: Boolean,
    @Json(name = "results")
    val results: List<Result>,
    @Json(name = "revenue")
    val revenue: Long?,
    @Json(name = "runtime")
    val runtime: Int?,
    @Json(name = "sort_by")
    val sortBy: String?,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)