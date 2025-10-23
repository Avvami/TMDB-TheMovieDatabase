package com.personal.tmdb.detail.presentation.images_preview

sealed interface ImagesPreviewUiEvent {
    data object OnNavigateBack: ImagesPreviewUiEvent
    data class SetUiHidden(val state: Boolean): ImagesPreviewUiEvent
    data class ShareImage(val filePath: String?): ImagesPreviewUiEvent
}