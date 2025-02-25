package com.personal.tmdb.core.domain.models

data class UpdateListDetailsRequest(
    val name: String,
    val description: String,
    val public: Boolean
)
