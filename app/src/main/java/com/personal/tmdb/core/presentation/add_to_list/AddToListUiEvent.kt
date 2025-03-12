package com.personal.tmdb.core.presentation.add_to_list

sealed interface AddToListUiEvent {
    data object OnNavigateBack: AddToListUiEvent
    data class AddToList(val listId: Int): AddToListUiEvent
    data class UpdateList(val watchlist: Boolean? = null, val favorite: Boolean? = null): AddToListUiEvent
    data class CreateMode(val creating: Boolean): AddToListUiEvent
    data class SetListVisibility(val public: Boolean): AddToListUiEvent
    data class SetListName(val text: String): AddToListUiEvent
    data class SetListDescription(val text: String): AddToListUiEvent
    data class CreateList(val name: String, val description: String, val public: Boolean): AddToListUiEvent
}