package com.personal.tmdb.detail.data.mappers

import com.personal.tmdb.core.domain.util.convertOffsetDateTimeToLocalDate
import com.personal.tmdb.detail.data.models.ReviewAuthorDetailsDto
import com.personal.tmdb.detail.data.models.ReviewDto
import com.personal.tmdb.detail.data.models.Reviews
import com.personal.tmdb.detail.domain.models.Review
import com.personal.tmdb.detail.domain.models.ReviewAuthorDetails
import com.personal.tmdb.detail.domain.models.ReviewsResponse

fun Reviews.toReviewsResponse(): ReviewsResponse {
    return ReviewsResponse(
        page = page,
        results = results.map { it.toReview() },
        totalPages = totalPages,
        totalResults = totalResults
    )
}

fun ReviewDto.toReview(): Review {
    val authorDetails = authorDetailsDto?.toReviewAuthorDetails()
    return Review(
        author = author,
        rating = authorDetails?.rating,
        authorDetails = authorDetails,
        content = content?.takeIf { it.isNotEmpty() },
        createdAt = convertOffsetDateTimeToLocalDate(createdAt),
        id = id,
        url = url
    )
}

fun ReviewAuthorDetailsDto.toReviewAuthorDetails(): ReviewAuthorDetails {
    return ReviewAuthorDetails(
        avatarPath = avatarPath,
        name = name,
        rating = rating?.toFloat(),
        username = username
    )
}