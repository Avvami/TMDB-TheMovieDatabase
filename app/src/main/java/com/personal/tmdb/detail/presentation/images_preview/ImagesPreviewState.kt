package com.personal.tmdb.detail.presentation.images_preview

data class ImagesPreviewState(
    val name: String,
    val selectedIndex: Int,
    val filePaths: List<String?>,
    val uiHidden: Boolean = false
)
