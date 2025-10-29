package com.personal.tmdb.core.domain.models

data class MyListsResponse(
    val page: Int,
    val results: List<MyList>,
    val totalPages: Int,
    val totalResults: Int
)
