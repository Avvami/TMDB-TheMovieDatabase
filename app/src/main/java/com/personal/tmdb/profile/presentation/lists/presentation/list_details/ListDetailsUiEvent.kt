package com.personal.tmdb.profile.presentation.lists.presentation.list_details

import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.navigation.Route

sealed interface ListDetailsUiEvent {
    data object OnNavigateBack: ListDetailsUiEvent
    data class OnNavigateTo(val route: Route): ListDetailsUiEvent
    data class SetEditingState(val editing: Boolean): ListDetailsUiEvent
    data class SetListVisibility(val public: Boolean): ListDetailsUiEvent
    data class SetListName(val text: String): ListDetailsUiEvent
    data class SetListDescription(val text: String): ListDetailsUiEvent
    data class UpdateListDetails(val listId: Int, val name: String, val description: String, val public: Boolean): ListDetailsUiEvent
    data class DeleteList(val listId: Int): ListDetailsUiEvent
    data class DeleteSelectedItems(val listId: Int, val items: List<MediaInfo>): ListDetailsUiEvent
    data class SetSelectEnabled(val enabled: Boolean): ListDetailsUiEvent
    data class AddSelectedItem(val mediaInfo: MediaInfo): ListDetailsUiEvent
    data class RemoveSelectedItem(val mediaInfo: MediaInfo): ListDetailsUiEvent
    data class GetListDetails(val listId: Int, val page: Int): ListDetailsUiEvent
}