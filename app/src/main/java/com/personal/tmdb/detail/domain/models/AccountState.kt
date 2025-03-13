package com.personal.tmdb.detail.domain.models

import com.personal.tmdb.detail.data.models.Rated

data class AccountState(
    val favorite: Boolean,
    val rated: Rated,
    val watchlist: Boolean
)
