package com.personal.tmdb.detail.domain.models

data class Images(
    val profiles: List<Image>?,
    val stills: List<Image>?,
    val backdrops: List<Image>?,
    val posters: List<Image>?,
    val logos: List<Image>?
)
