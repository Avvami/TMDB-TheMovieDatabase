package com.personal.tmdb.home.presentation.home

import androidx.paging.PagingData
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.DominantColors
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.detail.data.models.ImageDto
import kotlinx.coroutines.flow.Flow

data class HomeState(
    val loading: Boolean = false,
    val randomMedia: MediaInfo? = null,
    val randomMediaDominantColors: DominantColors? = null,
    val randomMediaLogos: List<ImageDto?>? = null,
    val trending: Flow<PagingData<MediaInfo>>? = null,
    val popularMovies: Flow<PagingData<MediaInfo>>? = null,
    val popularTvShows: Flow<PagingData<MediaInfo>>? = null,
    val upcomingMovies: Flow<PagingData<MediaInfo>>? = null,
    val errorMessage: UiText? = null
)