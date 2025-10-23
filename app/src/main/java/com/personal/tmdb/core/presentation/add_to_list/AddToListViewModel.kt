package com.personal.tmdb.core.presentation.add_to_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.CreateListRequest
import com.personal.tmdb.core.domain.models.LoadingProgress
import com.personal.tmdb.core.domain.models.MediaRequest
import com.personal.tmdb.core.domain.models.UpdateListMediaRequest
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.SnackbarController
import com.personal.tmdb.core.domain.util.SnackbarEvent
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.AddToList>()

    private val _addToListState = MutableStateFlow(
        AddToListState(
            favorite = false,
            watchlist = false
        )
    )
    val addToListState = _addToListState.asStateFlow()

    init {
        getLists(1)
    }

    private fun getLists(page: Int) {
        addToListState.value.lists?.let { listsResponse ->
            if (listsResponse.totalPages < page || addToListState.value.paging) {
                return
            }
            if (listsResponse.page != page) _addToListState.update { it.copy(paging = true) }
        }
        viewModelScope.launch {
            _addToListState.update {
                it.copy(
                    loadingLists = true,
                    errorMessage = null
                )
            }
            val user = userRepository.getUser()

            userRepository.getLists(accountObjectId = user?.accountObjectId ?: "", sessionId = user?.sessionId ?: "", page = page)
                .onError { error ->
                    _addToListState.update {
                        it.copy(
                            loadingLists = false,
                            paging = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _addToListState.update { state ->
                        val lists = state.lists
                        if (lists == null || page == 1) {
                            state.copy(
                                loadingLists = false,
                                paging = false,
                                lists = result
                            )
                        } else {
                            val mergedLists = lists.results + result.results
                            val updatedLists = lists.copy(
                                results = mergedLists,
                                page = result.page
                            )
                            state.copy(
                                loadingLists = false,
                                paging = false,
                                lists = updatedLists
                            )
                        }
                    }
                }
        }
    }

    private fun addToList(listId: Int) {
        viewModelScope.launch {
            updateLoadingProgress(listId, LoadingProgress.LOADING)
            val sessionId = userRepository.getUser()?.sessionId
            val request = UpdateListMediaRequest(
                items = listOf(
                    MediaRequest(
                        mediaType = routeData.mediaType,
                        mediaId = routeData.mediaId
                    )
                )
            )

            userRepository.addItemsToList(listId = listId, sessionId = sessionId ?: "", request)
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(message = error.toUiText())
                    )
                    updateLoadingProgress(listId, LoadingProgress.ERROR)
                    delay(1500L)
                    updateLoadingProgress(listId, LoadingProgress.STILL)
                }
                .onSuccess {
                    updateLoadingProgress(listId, LoadingProgress.SUCCESS)
                    delay(1500L)
                    updateLoadingProgress(listId, LoadingProgress.STILL)
                }
        }
    }

    private fun updateLoadingProgress(listId: Int, progress: LoadingProgress) {
        _addToListState.update { state ->
            val updatedList = state.lists?.results?.map { list ->
                if (list.id == listId) {
                    list.copy(loadingProgress = progress)
                } else {
                    list
                }
            }
            state.copy(
                lists = state.lists?.copy(results = updatedList ?: state.lists.results)
            )
        }
    }

    private fun updateList(watchlist: Boolean?, favorite: Boolean?) {
        viewModelScope.launch {
            _addToListState.update {
                it.copy(
                    watchlistLoading = watchlist != null,
                    favoriteLoading = favorite != null
                )
            }
            val user = userRepository.getUser()
            val request = MediaRequest(
                mediaType = routeData.mediaType,
                mediaId = routeData.mediaId,
                watchlist = watchlist,
                favorite = favorite
            )
            watchlist?.let { watchlist ->
                userRepository.updateWatchlistItem(accountId = user?.accountId ?: 0, sessionId = user?.sessionId ?: "", mediaRequest = request)
                    .onError { error ->
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(message = error.toUiText())
                        )
                        _addToListState.update { it.copy(watchlistLoading = false) }
                    }
                    .onSuccess {
                        _addToListState.update {
                            it.copy(
                                watchlist = watchlist,
                                watchlistLoading = false
                            )
                        }
                    }
            }
            favorite?.let { favorite ->
                userRepository.updateFavoriteItem(accountId = user?.accountId ?: 0, sessionId = user?.sessionId ?: "", mediaRequest = request)
                    .onError { error ->
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(message = error.toUiText())
                        )
                        _addToListState.update { it.copy(favoriteLoading = false) }
                    }
                    .onSuccess {
                        _addToListState.update {
                            it.copy(
                                favorite = favorite,
                                favoriteLoading = false
                            )
                        }
                    }
            }
        }
    }

    private fun createList(name: String, description: String, public: Boolean) {
        viewModelScope.launch {
            _addToListState.update { it.copy(creating = true) }

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
                    _addToListState.update { it.copy(creating = false) }
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = error.toUiText()
                        )
                    )
                }
                .onSuccess {
                    _addToListState.update {
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

    fun addToListUiEvent(event: AddToListUiEvent) {
        when (event) {
            AddToListUiEvent.OnNavigateBack -> Unit
            is AddToListUiEvent.AddToList -> {
                addToList(event.listId)
            }
            is AddToListUiEvent.UpdateList -> {
                updateList(event.watchlist, event.favorite)
            }
            is AddToListUiEvent.CreateMode -> {
                _addToListState.update {
                    it.copy(
                        createEnabled = event.creating,
                        listName = "",
                        listDescription = ""
                    )
                }
            }
            is AddToListUiEvent.CreateList -> {
                createList(event.name, event.description, event.public)
            }
            is AddToListUiEvent.SetListDescription -> {
                _addToListState.update { it.copy(listDescription = event.text) }
            }
            is AddToListUiEvent.SetListName -> {
                _addToListState.update { it.copy(listName = event.text) }
            }
            is AddToListUiEvent.SetListVisibility -> {
                _addToListState.update { it.copy(publicList = event.public) }
            }
            is AddToListUiEvent.GetLists -> {
                getLists(event.page)
            }
        }
    }
}