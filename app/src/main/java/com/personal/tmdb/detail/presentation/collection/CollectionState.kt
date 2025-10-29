package com.personal.tmdb.detail.presentation.collection

import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.detail.data.models.GenreDto
import com.personal.tmdb.detail.domain.models.CollectionInfo
import com.personal.tmdb.detail.domain.util.CollectionSortType

data class CollectionState(
    val collectionId: Int = 0,
    val collectionInfo: CollectionInfo? = null,
    val genres: List<GenreDto> = emptyList(),
    val originalParts: List<MediaInfo>? = null,
    val sortType: CollectionSortType? = null,
    val displayingSortType: UiText = UiText.DynamicString(""),
    val loading: Boolean = false,
    val errorMessage: UiText? = null
)
