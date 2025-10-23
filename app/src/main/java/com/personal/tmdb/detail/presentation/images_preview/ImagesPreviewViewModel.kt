package com.personal.tmdb.detail.presentation.images_preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.personal.tmdb.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImagesPreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.ImagesPreview>()

    private val _imagesPreviewState = MutableStateFlow(
        ImagesPreviewState(
            name = routeData.mediaName,
            selectedIndex = routeData.selectedIndex,
            filePaths = routeData.images
        )
    )
    val imagesPreviewState = _imagesPreviewState.asStateFlow()

    fun imagesPreviewUiEvent(event: ImagesPreviewUiEvent) {
        when (event) {
            ImagesPreviewUiEvent.OnNavigateBack -> Unit
            is ImagesPreviewUiEvent.SetUiHidden -> {
                _imagesPreviewState.update { it.copy(uiHidden = event.state) }
            }
            is ImagesPreviewUiEvent.ShareImage -> Unit
        }
    }
}