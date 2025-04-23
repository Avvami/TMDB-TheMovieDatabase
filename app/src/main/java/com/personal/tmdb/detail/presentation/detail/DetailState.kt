package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.core.data.remote.CountryCode
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.detail.domain.models.AccountState
import com.personal.tmdb.detail.domain.models.CollectionInfo
import com.personal.tmdb.detail.domain.models.CountryName
import com.personal.tmdb.detail.domain.models.MediaDetailInfo

data class DetailState(
    val mediaType: MediaType,
    val mediaId: Int,
    val uiState: DetailUiState = DetailUiState.CONTENT,
    val accountState: AccountState? = null,
    val details: MediaDetailInfo? = null,
    val collection: CollectionInfo? = null,
    val watchCountry: String = "",
    val watchCountries: Map<CountryCode, CountryName>? = null,
    val loading: Boolean = false,
    val rating: Boolean = false,
    val showRatingSheet: Boolean = false,
    val errorMessage: UiText? = null
)
