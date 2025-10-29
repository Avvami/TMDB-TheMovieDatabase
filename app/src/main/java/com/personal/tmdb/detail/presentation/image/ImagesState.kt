package com.personal.tmdb.detail.presentation.image

import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.detail.data.models.ImageDto
import com.personal.tmdb.detail.data.models.ImagesDto
import com.personal.tmdb.detail.domain.util.ImageType

data class ImagesState(
    val initialPage: Int?,
    val imageType: ImageType,
    val state: ImagesDto? = null,
    val imagesByType: List<ImageDto?>? = null,
    val loading: Boolean = false,
    val errorMessage: UiText? = null,
    val showGridView: Boolean = initialPage == null,
    val hideUi: Boolean = false
)
