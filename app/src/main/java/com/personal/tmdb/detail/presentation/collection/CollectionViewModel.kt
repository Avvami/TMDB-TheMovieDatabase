package com.personal.tmdb.detail.presentation.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.domain.repository.DetailRepository
import com.personal.tmdb.detail.domain.util.CollectionSortType
import com.personal.tmdb.detail.domain.util.convertCollectionSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val detailRepository: DetailRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.Collection>()

    private val _collectionState = MutableStateFlow(
        CollectionState(
            collectionId = routeData.collectionId
        )
    )
    val collectionState = _collectionState.asStateFlow()

    init {
        getCollection(
            collectionId = routeData.collectionId
        )
    }

    private fun getCollection(collectionId: Int, language: String? = null) {
        viewModelScope.launch {
            _collectionState.update { it.copy(loading = true) }

            detailRepository.getCollection(collectionId, language)
                .onError { error ->
                    _collectionState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _collectionState.update {
                        it.copy(
                            loading = false,
                            collectionInfo = result,
                            originalParts = result.parts
                        )
                    }
                }
        }
    }

    private fun sortCollectionParts(sortType: CollectionSortType?) {
        _collectionState.update { state ->
            val sortedParts = state.collectionInfo?.let { info ->
                when (sortType) {
                    CollectionSortType.RATING_ASC -> info.parts?.sortedBy { it.voteAverage }
                    CollectionSortType.RATING_DESC -> info.parts?.sortedByDescending { it.voteAverage }
                    CollectionSortType.RELEASE_DATE_ASC -> info.parts?.sortedBy { it.releaseDate }
                    CollectionSortType.RELEASE_DATE_DESC -> info.parts?.sortedByDescending { it.releaseDate }
                    null -> collectionState.value.originalParts
                }
            }
            state.copy(
                collectionInfo = state.collectionInfo?.copy(parts = sortedParts),
                sortType = sortType,
                displayingSortType = if (sortType != null) convertCollectionSortType(sortType) else state.displayingSortType
            )
        }
    }

    fun collectionUiEvent(event: CollectionUiEvent) {
        when (event) {
            CollectionUiEvent.OnNavigateBack -> {}
            is CollectionUiEvent.OnNavigateTo -> {}
            is CollectionUiEvent.SetSortType -> {
                sortCollectionParts(event.sortType)
            }
        }
    }
}