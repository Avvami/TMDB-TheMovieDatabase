package com.personal.tmdb.profile.presentation.lists.presentation.lists

import com.personal.tmdb.core.domain.models.ListInfo
import com.personal.tmdb.core.navigation.Route

sealed interface ListsUiEvent {
    data object OnNavigateBack: ListsUiEvent
    data class OnNavigateTo(val route: Route): ListsUiEvent
    data class GetLists(val page: Int): ListsUiEvent
    data class CreateMode(val creating: Boolean): ListsUiEvent
    data class SetListVisibility(val public: Boolean): ListsUiEvent
    data class SetListName(val text: String): ListsUiEvent
    data class SetListDescription(val text: String): ListsUiEvent
    data class CreateList(val name: String, val description: String, val public: Boolean): ListsUiEvent
    data class DeleteSelectedLists(val items: List<ListInfo>): ListsUiEvent
    data class SetSelectEnabled(val enabled: Boolean): ListsUiEvent
    data class AddSelectedItem(val listInfo: ListInfo): ListsUiEvent
    data class ReplaceSelectedItem(val listInfo: ListInfo): ListsUiEvent
    data class RemoveSelectedItem(val listInfo: ListInfo): ListsUiEvent
}