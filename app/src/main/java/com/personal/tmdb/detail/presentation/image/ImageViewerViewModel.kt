package com.personal.tmdb.detail.presentation.image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.detail.data.models.ImageDto
import com.personal.tmdb.detail.domain.repository.DetailRepository
import com.personal.tmdb.detail.domain.util.ImageType
import com.personal.tmdb.detail.domain.util.convertImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val detailRepository: DetailRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.Images>()

    private val _imagesState = MutableStateFlow(
        ImagesState(
            initialPage = routeData.selectedImageIndex,
            imageType = convertImageType(routeData.imageType)
        )
    )
    val imagesState = _imagesState.asStateFlow()

    init {
        getImages(
            path = routeData.imagesPath,
            includeImageLanguage = "en,null"
        )
    }

    private fun getImages(
        path: String,
        language: String? = null,
        includeImageLanguage: String? = null
    ) {
        viewModelScope.launch {
            _imagesState.update { it.copy(loading = true) }

            detailRepository.getImages(path, language, includeImageLanguage)
                .onError { error ->
                    _imagesState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    val images: List<ImageDto?>? = when (imagesState.value.imageType) {
                        ImageType.PROFILES -> result.profiles
                        ImageType.STILLS -> result.stills
                        ImageType.BACKDROPS -> result.backdrops
                        ImageType.POSTERS -> result.posters
                        ImageType.UNKNOWN -> null
                    }

                    _imagesState.update {
                        it.copy(
                            loading = false,
                            state = result,
                            imagesByType = images
                        )
                    }
                }
        }
    }

    fun imageViewerUiEvent(event: ImageViewerUiEvent) {
        when (event) {
            ImageViewerUiEvent.OnNavigateBack -> {}
            ImageViewerUiEvent.ChangeShowGridViewState -> {
                _imagesState.update { it.copy(showGridView = !it.showGridView) }
            }
            ImageViewerUiEvent.ChangeHideUiState -> {
                _imagesState.update { it.copy(hideUi = !it.hideUi) }
            }
            is ImageViewerUiEvent.SetImageType -> {
                val imageType = event.imageType
                _imagesState.update {
                    it.copy(
                        imageType = imageType,
                        imagesByType = when (imageType) {
                            ImageType.BACKDROPS -> it.state?.backdrops
                            ImageType.POSTERS -> it.state?.posters
                            else -> null
                        }
                    )
                }
            }
        }
    }
}