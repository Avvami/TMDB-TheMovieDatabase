package com.personal.tmdb.profile.presentation.lists.presentation.list_details

import com.personal.tmdb.core.navigation.Route

sealed interface ListDetailsUiEvent {
    data object OnNavigateBack: ListDetailsUiEvent
    data class OnNavigateTo(val route: Route): ListDetailsUiEvent
    data object ChangeEditListState: ListDetailsUiEvent
    data class SetListVisibility(val public: Boolean): ListDetailsUiEvent
    data class SetListName(val text: String): ListDetailsUiEvent
    data class SetListDescription(val text: String): ListDetailsUiEvent
    data object DeleteList: ListDetailsUiEvent
    data class DeleteItem(val mediaId: Int): ListDetailsUiEvent
}