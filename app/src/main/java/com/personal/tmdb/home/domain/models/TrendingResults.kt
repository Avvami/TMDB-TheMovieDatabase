package com.personal.tmdb.home.domain.models

import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DominantColors
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.detail.data.models.Image

data class TrendingResult(
    val trending: MediaResponseInfo? = null,
    val randomMedia: MediaInfo? = null,
    val randomMediaDominantColors: DominantColors? = null,
    val randomMediaLogos: List<Image?>? = null,
    val errorMessage: UiText? = null
)
