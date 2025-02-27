package com.personal.tmdb.profile.presentation.lists.presentation.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.CreateListRequest
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
        viewModelScope.launch {
            _listsState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }
            val user = userRepository.getUser()

            userRepository.getLists(accountObjectId = user?.accountObjectId ?: "", sessionId = user?.sessionId ?: "", page = page)
                .onError { error ->
                    _listsState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _listsState.update {
                        it.copy(
                            loading = false,
                            lists = result
                        )
                    }
                }
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
                    getLists(1)
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = UiText.StringResource(R.string.created_successfully)
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
        }
    }
}