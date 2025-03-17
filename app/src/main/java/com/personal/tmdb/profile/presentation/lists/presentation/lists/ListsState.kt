package com.personal.tmdb.profile.presentation.lists.presentation.lists

import com.personal.tmdb.core.domain.models.ListInfo
import com.personal.tmdb.core.domain.models.ListsResponseInfo
import com.personal.tmdb.core.domain.util.UiText

data class ListsState(
    val loading: Boolean = false,
    val paging: Boolean = false,
    val lists: ListsResponseInfo? = null,
    val createEnabled: Boolean = false,
    val listName: String = "",
    val listDescription: String = "",
    val publicList: Boolean = true,
    val creating: Boolean = false,
    val selectEnabled: Boolean = false,
    val selectedItems: List<ListInfo> = emptyList(),
    val deleting: Boolean = false,
    val errorMessage: UiText? = null
)
