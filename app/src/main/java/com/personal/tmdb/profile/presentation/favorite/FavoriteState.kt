package com.personal.tmdb.profile.presentation.favorite

import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.UiText

data class FavoriteState(
    val loading: Boolean = false,
    val favorite: MediaResponseInfo? = null,
    val mediaType: MediaType = MediaType.TV,
    val errorMessage: UiText? = null
)
