package com.personal.tmdb.detail.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewDto(
    @Json(name = "author")
    val author: String?,
    @Json(name = "author_details")
    val authorDetailsDto: ReviewAuthorDetailsDto?,
    @Json(name = "content")
    val content: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "updated_at")
    val updatedAt: String?,
    @Json(name = "url")
    val url: String?
)