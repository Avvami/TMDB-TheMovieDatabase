package com.personal.tmdb.detail.domain.models

data class Available(
    val link: String,
    val ads: List<Provider>?,
    val streaming: List<Provider>?,
    val free: List<Provider>?,
    val buy: List<Provider>?,
    val rent: List<Provider>?
)