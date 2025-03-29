package com.personal.tmdb

import com.personal.tmdb.core.domain.models.User
import com.personal.tmdb.core.domain.util.DominantColors
import com.personal.tmdb.core.domain.util.UiText

data class UserState(
    val loading: Boolean = false,
    val user: User? = null,
    val dominantColor: DominantColors? = null,
    val errorMessage: UiText? = null,
    val requestToken: String? = null
)
