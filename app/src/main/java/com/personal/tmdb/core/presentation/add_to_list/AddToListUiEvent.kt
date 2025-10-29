package com.personal.tmdb.core.presentation.add_to_list

import com.personal.tmdb.core.presentation.LoadState

sealed interface AddToListUiEvent {
    data object OnNavigateBack: AddToListUiEvent
    data class AddToList(val listId: Int): AddToListUiEvent
    data class SetListLoadState(val listId: Int, val loadState: LoadState): AddToListUiEvent
}