package com.personal.tmdb.core.presentation.add_to_list

import com.personal.tmdb.core.domain.models.ListsResponseInfo
import com.personal.tmdb.core.domain.util.UiText

data class AddToListState(
    val favorite: Boolean,
    val favoriteLoading: Boolean = false,
    val watchlist: Boolean,
    val watchlistLoading: Boolean = false,
    val lists: ListsResponseInfo? = null,
    val loadingLists: Boolean = false,
    val createEnabled: Boolean = false,
    val listName: String = "",
    val listDescription: String = "",
    val publicList: Boolean = true,
    val creating: Boolean = false,
    val errorMessage: UiText? = null
)
