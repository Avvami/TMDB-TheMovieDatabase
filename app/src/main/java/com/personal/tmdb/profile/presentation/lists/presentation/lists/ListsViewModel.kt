package com.personal.tmdb.profile.presentation.lists.presentation.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.CreateListRequest
import com.personal.tmdb.core.domain.models.MyList
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.SnackbarController
import com.personal.tmdb.core.domain.util.SnackbarEvent
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _listsState = MutableStateFlow(ListsState())
    val listsState = _listsState.asStateFlow()

    private fun getLists(page: Int) {
        listsState.value.lists?.let { listsResponse ->
            if (listsResponse.totalPages < page || listsState.value.paging) {
                return
            }
            if (listsResponse.page != page) _listsState.update { it.copy(paging = true) }
        }
        viewModelScope.launch {
            _listsState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }
            val user = userRepository.getUser()

//            userRepository.getLists(
//                accountObjectId = user?.accountObjectId ?: "",
//                sessionId = user?.sessionId ?: ""
//            )
//                .onError { error ->
//                    _listsState.update {
//                        it.copy(
//                            loading = false,
//                            paging = false,
//                            errorMessage = error.toUiText()
//                        )
//                    }
//                }
//                .onSuccess { result ->
//                    _listsState.update { state ->
//                        val lists = state.lists
//                        if (lists == null || page == 1) {
//                            state.copy(
//                                loading = false,
//                                paging = false,
//                                lists = result
//                            )
//                        } else {
//                            val mergedLists = lists.results + result.results
//                            val updatedLists = lists.copy(
//                                results = mergedLists,
//                                page = result.page
//                            )
//                            state.copy(
//                                loading = false,
//                                paging = false,
//                                lists = updatedLists
//                            )
//                        }
//                    }
//                }
        }
    }

    private fun createList(name: String, description: String, public: Boolean) {
        viewModelScope.launch {
            _listsState.update { it.copy(creating = true) }

            val user = userRepository.getUser()
            val request = CreateListRequest(
                name = name.trim(),
                description = description.trim(),
                iso31661 = user?.iso31661,
                iso6391 = user?.iso6391,
                public = public
            )

            userRepository.createList(sessionId = user?.sessionId ?: "", createListRequest = request)
                .onError { error ->
                    _listsState.update { it.copy(creating = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = error.toUiText()
                        )
                    )
                }
                .onSuccess {
                    _listsState.update {
                        it.copy(
                            createEnabled = false,
                            creating = false,
                            listName = "",
                            listDescription = "",
                            publicList = false
                        )
                    }
                    getLists(page = 1)
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = UiText.StringResource(R.string.created_successfully)
                        )
                    )
                }
        }
    }

    private fun deleteList(myList: MyList) {
        viewModelScope.launch {
            _listsState.update { it.copy(deleting = true) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""

            userRepository.deleteList(myList.id, sessionId)
                .onError { error ->
                    _listsState.update { it.copy(deleting = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = error.toUiText()
                        )
                    )
                }
                .onSuccess {
                    _listsState.update { state ->
                        val results = state.lists?.results.orEmpty() - myList
                        val updatedSelectedItems = state.selectedItems - myList
                        val updatedLists = state.lists?.copy(
                            results = results
                        )
                        state.copy(
                            lists = updatedLists,
                            deleting = false,
                            selectedItems = updatedSelectedItems
                        )
                    }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = UiText.StringResource(R.string.deleted_successfully)
                        )
                    )
                }
        }
    }

    fun listsUiEvent(event: ListsUiEvent) {
        when (event) {
            ListsUiEvent.OnNavigateBack -> {}
            is ListsUiEvent.OnNavigateTo -> {}
            is ListsUiEvent.GetLists -> {
                getLists(event.page)
            }
            is ListsUiEvent.CreateList -> {
                createList(event.name, event.description, event.public)
            }
            is ListsUiEvent.CreateMode -> {
                if (event.creating) {
                    _listsState.update { it.copy(createEnabled = true) }
                } else {
                    _listsState.update {
                        it.copy(
                            createEnabled = false,
                            listName = "",
                            listDescription = ""
                        )
                    }
                }
            }
            is ListsUiEvent.SetListDescription -> {
                _listsState.update { it.copy(listDescription = event.text) }
            }
            is ListsUiEvent.SetListName -> {
                _listsState.update { it.copy(listName = event.text) }
            }
            is ListsUiEvent.SetListVisibility -> {
                _listsState.update { it.copy(publicList = event.public) }
            }
            is ListsUiEvent.AddSelectedItem -> {
                _listsState.update { it.copy(selectedItems = it.selectedItems + event.myList) }
            }
            is ListsUiEvent.RemoveSelectedItem -> {
                _listsState.update { it.copy(selectedItems = it.selectedItems - event.myList) }
            }
            is ListsUiEvent.ReplaceSelectedItem -> {
                _listsState.update { it.copy(selectedItems = listOf(event.myList)) }
            }
            is ListsUiEvent.SetSelectEnabled -> {
                if (event.enabled) {
                    _listsState.update { it.copy(selectEnabled = true) }
                } else {
                    _listsState.update { it.copy(selectEnabled = false, selectedItems = emptyList()) }
                }
            }
            is ListsUiEvent.DeleteSelectedLists -> {
                deleteList(event.items.first())
            }
        }
    }
}