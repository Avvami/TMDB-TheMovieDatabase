package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.detail.domain.models.CollectionInfo
import com.personal.tmdb.detail.domain.models.MediaDetailInfo

data class DetailState(
    val mediaType: MediaType,
    val mediaId: Int,
    val accountState: AccountState? = null,
    val details: MediaDetailInfo? = null,
    val collection: CollectionInfo? = null,
    val watchCountry: String = "",
    val showMoreDetails: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: UiText? = null
)
