package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.core.data.remote.CountryCode
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.data.models.Image
import com.personal.tmdb.detail.domain.models.AccountState
import com.personal.tmdb.detail.domain.models.CollectionInfo
import com.personal.tmdb.detail.domain.models.CountryName
import com.personal.tmdb.detail.domain.models.MediaDetail

data class DetailState(
    val mediaType: MediaType,
    val mediaId: Int,
    val uiState: DetailUiState = DetailUiState.CONTENT,
    val loadState: LoadState = LoadState.NotLoading,
    val accountState: AccountState? = null,
    val details: MediaDetail? = null,
    val logo: Image? = null,
    val collection: CollectionInfo? = null,
    val watchCountry: String = "",
    val watchCountries: Map<CountryCode, CountryName>? = null,
    val loading: Boolean = false,
    val rating: Boolean = false,
    val showRatingSheet: Boolean = false,
    val errorMessage: UiText? = null
)
