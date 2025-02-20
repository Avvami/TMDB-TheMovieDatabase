package com.personal.tmdb.profile.presentation.lists.presentation.list_details

import com.personal.tmdb.core.domain.models.ListDetailsInfo
import com.personal.tmdb.core.domain.util.UiText

data class ListDetailsState(
    val listId: Int,
    val listDetails: ListDetailsInfo? = null,
    val editing: Boolean = false,
    val listName: String = "",
    val listDescription: String = "",
    val publicList: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: UiText? = null
)
