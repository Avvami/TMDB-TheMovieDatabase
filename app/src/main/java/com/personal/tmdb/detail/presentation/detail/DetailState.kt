package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.core.domain.util.CountryCode
import com.personal.tmdb.core.domain.util.CountryName
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.data.models.Image
import com.personal.tmdb.detail.domain.models.AccountState
import com.personal.tmdb.detail.domain.models.CollectionInfo
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.domain.models.Review

data class DetailState(
    val mediaType: MediaType,
    val mediaId: Int,
    val uiState: DetailUiState = DetailUiState.CONTENT,
    val loadState: LoadState = LoadState.NotLoading,
    val accountState: AccountState? = null,
    val details: MediaDetail? = null,
    val logo: Image? = null,
    val dimTopAppBar: Boolean = false,
    val collection: CollectionInfo? = null,
    val watchCountries: Map<CountryCode, CountryName>? = null,
    val selectedCountry: Map.Entry<CountryCode, CountryName>? = null,
    val loading: Boolean = false,
    val rating: Boolean = false,
    val showRatingSheet: Boolean = false,
    val selectedReview: Review? = null
)
