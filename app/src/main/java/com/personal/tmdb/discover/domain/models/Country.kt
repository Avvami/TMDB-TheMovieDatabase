package com.personal.tmdb.discover.domain.models

import java.util.Locale

data class Country(
    val code: String,
    val englishName: String?,
    val locale: Locale
)
