package com.personal.tmdb.profile.presentation.lists.presentation.list_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaRequest
import com.personal.tmdb.core.domain.models.UpdateListMediaRequest
import com.personal.tmdb.core.domain.models.UpdateListDetailsRequest
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.SnackbarController
import com.personal.tmdb.core.domain.util.SnackbarEvent
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.ListDetails>()

    private val _listDetailsState = MutableStateFlow(ListDetailsState(listId = routeData.listId))
    val listDetailsState = _listDetailsState.asStateFlow()

    private fun getListDetails(listId: Int, page: Int) {
        listDetailsState.value.listDetails?.let { details ->
            if (details.totalPages < page || listDetailsState.value.paging) {
                return
            }
            if (details.page != page) _listDetailsState.update { it.copy(paging = true) }
        }
        viewModelScope.launch {
            _listDetailsState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }

            val language = preferencesRepository.getLanguage()
            val sessionId = userRepository.getUser()?.sessionId ?: ""

            userRepository.getListDetails(listId, sessionId, page, language)
                .onError { error ->
                    _listDetailsState.update {
                        it.copy(
                            loading = false,
                            paging = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _listDetailsState.update { state ->
                        val details = state.listDetails
                        if (details == null || page == 1) {
                            state.copy(
                                loading = false,
                                paging = false,
                                listDetails = result,
                                listName = result.name ?: "",
                                listDescription = result.description ?: "",
                                publicList = result.public
                            )
                        } else {
                            val mergedResults = details.results.orEmpty() + result.results.orEmpty()
                            val updatedDetails = details.copy(
                                results = mergedResults,
                                page = result.page
                            )
                            state.copy(
                                loading = false,
                                paging = false,
                                listDetails = updatedDetails
                            )
                        }
                    }
                }
        }
    }

    private fun deleteSelectedItems(listId: Int, items: List<MediaInfo>) {
        viewModelScope.launch {
            _listDetailsState.update { it.copy(deleting = true) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""
            val request = UpdateListMediaRequest(
                items = items.map {
                    MediaRequest(
                        mediaType = it.mediaType?.name?.lowercase() ?: MediaType.UNKNOWN.name.lowercase(),
                        mediaId = it.id
                    )
                }
            )

            userRepository.deleteListItems(listId, sessionId, request)
                .onError { error ->
                    _listDetailsState.update { it.copy(deleting = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = error.toUiText()
                        )
                    )
                }
                .onSuccess {
                    _listDetailsState.update { state ->
                        val details = state.listDetails
                        val updatedItems = details?.results.orEmpty() - items.toSet()
                        val updatedDetails = details?.copy(
                            results = updatedItems,
                            itemCount = details.itemCount - items.size
                        )
                        val updatedSelectedItems = state.selectedItems - items.toSet()
                        state.copy(
                            listDetails = updatedDetails,
                            selectedItems = updatedSelectedItems,
                            deleting = false
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

    private fun updateListDetails(listId: Int, name: String, description: String, public: Boolean) {
        viewModelScope.launch {
            _listDetailsState.update { it.copy(updating = true) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""
            val request = UpdateListDetailsRequest(name.trim(), description.trim(), public)

            userRepository.updateListDetails(listId, sessionId, request)
                .onError { error ->
                    _listDetailsState.update { it.copy(updating = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = error.toUiText()
                        )
                    )
                }
                .onSuccess {
                    _listDetailsState.update {
                        it.copy(
                            editing = it.selectedItems.isNotEmpty(),
                            updating = false
                        )
                    }
                    getListDetails(listId = listId, page = 1)
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = UiText.StringResource(R.string.updated_successfully)
                        )
                    )
                }
        }
    }

    private val _deleteListChannel = Channel<ListDetailsUiEvent>()
    val deleteListChannelFlow = _deleteListChannel.receiveAsFlow()

    private fun deleteList(listId: Int) {
        viewModelScope.launch {
            _listDetailsState.update { it.copy(deletingList = true) }

            val sessionId = userRepository.getUser()?.sessionId ?: ""

            userRepository.deleteList(listId, sessionId)
                .onError { error ->
                    _listDetailsState.update { it.copy(deletingList = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = error.toUiText()
                        )
                    )
                }
                .onSuccess {
                    _listDetailsState.update { it.copy(deletingList = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = UiText.StringResource(R.string.deleted_successfully)
                        )
                    )
                    _deleteListChannel.send(ListDetailsUiEvent.OnNavigateBack)
                }
        }
    }

    fun listDetailsUiEvent(event: ListDetailsUiEvent) {
        when (event) {
            ListDetailsUiEvent.OnNavigateBack -> {}
            is ListDetailsUiEvent.OnNavigateTo -> {}
            is ListDetailsUiEvent.SetEditingState -> {
                if (event.editing) {
                    _listDetailsState.update { it.copy(editing = true) }
                } else {
                    _listDetailsState.update {
                        it.copy(
                            editing = false,
                            listName = it.listDetails?.name ?: "",
                            listDescription = it.listDetails?.description ?: "",
                            publicList = it.listDetails?.public ?: it.publicList,
                            selectedItems = emptyList()
                        )
                    }
                }
            }
            is ListDetailsUiEvent.SetListVisibility -> {
                _listDetailsState.update { it.copy(publicList = event.public) }
            }
            is ListDetailsUiEvent.SetListDescription -> {
                _listDetailsState.update { it.copy(listDescription = event.text) }
            }
            is ListDetailsUiEvent.SetListName -> {
                _listDetailsState.update { it.copy(listName = event.text) }
            }
            is ListDetailsUiEvent.UpdateListDetails -> {
                updateListDetails(event.listId, event.name, event.description, event.public)
            }
            is ListDetailsUiEvent.DeleteList -> {
                deleteList(event.listId)
            }
            is ListDetailsUiEvent.DeleteSelectedItems -> {
                deleteSelectedItems(event.listId, event.items)
            }
            is ListDetailsUiEvent.AddSelectedItem -> {
                _listDetailsState.update { it.copy(selectedItems = it.selectedItems + event.mediaInfo) }
            }
            is ListDetailsUiEvent.RemoveSelectedItem -> {
                _listDetailsState.update { it.copy(selectedItems = it.selectedItems - event.mediaInfo) }
            }
            is ListDetailsUiEvent.SetSelectEnabled -> {
                if (event.enabled) {
                    _listDetailsState.update { it.copy(selectEnabled = true) }
                } else {
                    _listDetailsState.update { it.copy(selectEnabled = false, selectedItems = emptyList()) }
                }
            }
            is ListDetailsUiEvent.GetListDetails -> {
                getListDetails(event.listId, event.page)
            }
        }
    }
}