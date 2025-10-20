package com.personal.tmdb.detail.domain.models

data class Provider(
    val displayPriority: Int,
    val logoPath: String?,
    val providerId: Int,
    val providerName: String?
)
