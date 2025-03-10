package com.personal.tmdb.detail.presentation.detail

import com.personal.tmdb.detail.data.models.Rated

data class AccountState(
    val favorite: Boolean,
    val rated: Rated,
    val watchlist: Boolean
)
