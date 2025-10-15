package com.personal.tmdb.detail.presentation.episodes

import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.detail.domain.models.MediaDetail
import com.personal.tmdb.detail.domain.models.SeasonInfo

data class SeasonState(
    val mediaId: Int = 0,
    val seasonNumber: Int = 0,
    val mediaDetail: MediaDetail? = null,
    val seasonInfo: SeasonInfo? = null,
    val overviewCollapsed: Boolean = true,
    val loading: Boolean = false,
    val errorMessage: UiText? = null
)
