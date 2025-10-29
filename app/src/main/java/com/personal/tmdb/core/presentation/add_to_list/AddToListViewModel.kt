package com.personal.tmdb.core.presentation.add_to_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.personal.tmdb.core.domain.models.MediaRequest
import com.personal.tmdb.core.domain.models.MyList
import com.personal.tmdb.core.domain.models.UpdateListMediaRequest
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.AddToList>()

    // Needs to be cleaned if there will ever be .refresh() call on paging data
    private val listsUiActions = MutableStateFlow<List<AddToListUiEvent>>(emptyList())

    private val _addToListState = MutableStateFlow(AddToListState())
    val addToListState = _addToListState
        .onStart { getLists() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000L),
            AddToListState()
        )

    private fun getLists() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            listsUiActions.update { emptyList() }
            println(_addToListState.value)

            val myListsResults = userRepository.getLists(
                accountObjectId = user?.accountObjectId ?: "",
                sessionId = user?.sessionId ?: ""
            ).flow.cachedIn(viewModelScope).combine(listsUiActions) { pagingData, actions ->
                actions.fold(pagingData) { acc, action ->
                    applyAction(acc, action)
                }
            }

            _addToListState.update { it.copy(myListsResults = myListsResults) }
        }
    }

    private fun applyAction(
        pagingData: PagingData<MyList>,
        event: AddToListUiEvent?
    ): PagingData<MyList> {
        return when (event) {
            is AddToListUiEvent.SetListLoadState -> {
                pagingData.map { myList ->
                    if (event.listId == myList.id) {
                        myList.copy(loadState = event.loadState)
                    } else {
                        myList
                    }
                }
            }
            else -> pagingData
        }
    }

    private fun addToList(listId: Int) {
        viewModelScope.launch {
            addToListUiEvent(
                AddToListUiEvent.SetListLoadState(
                    listId = listId,
                    loadState = LoadState.Loading
                )
            )
            val sessionId = userRepository.getUser()?.sessionId
            val request = UpdateListMediaRequest(
                items = listOf(
                    MediaRequest(
                        mediaType = routeData.mediaType,
                        mediaId = routeData.mediaId
                    )
                )
            )

            userRepository.addItemsToList(
                listId = listId,
                sessionId = sessionId ?: "",
                updateListMediaRequest = request
            ).onError { error ->
                addToListUiEvent(
                    AddToListUiEvent.SetListLoadState(
                        listId = listId,
                        loadState = LoadState.Error(error.toUiText())
                    )
                )
                delay(3000L)
                addToListUiEvent(
                    AddToListUiEvent.SetListLoadState(
                        listId = listId,
                        loadState = LoadState.NotLoading
                    )
                )
            }.onSuccess {
                addToListUiEvent(
                    AddToListUiEvent.SetListLoadState(
                        listId = listId,
                        loadState = LoadState.Success
                    )
                )
                delay(1500L)
                addToListUiEvent(
                    AddToListUiEvent.SetListLoadState(
                        listId = listId,
                        loadState = LoadState.NotLoading
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
            is AddToListUiEvent.SetListLoadState -> {
                listsUiActions.update { it + event }
            }
        }
    }
}