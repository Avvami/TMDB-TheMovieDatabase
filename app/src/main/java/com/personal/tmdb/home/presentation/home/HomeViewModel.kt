package com.personal.tmdb.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.repository.DominantColorRepository
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.core.domain.util.onError
import com.personal.tmdb.core.domain.util.onSuccess
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.detail.domain.repository.DetailRepository
import com.personal.tmdb.home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val detailRepository: DetailRepository,
    private val dominantColorRepository: DominantColorRepository
): ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        _homeState.update { it.copy(loading = true) }
        getTrendingList(TimeWindow.DAY)
        getNowPlaying()
    }

    private fun getTrendingList(timeWindow: TimeWindow, language: String? = null) {
        viewModelScope.launch {
            homeRepository.getTrendingList(timeWindow, language)
                .onError { error ->
                    _homeState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    val randomMedia = result.results.randomOrNull()
                    val randomMediaDominantColors = dominantColorRepository.calculateDominantColor(
                        imageUrl = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W780 + randomMedia?.backdropPath
                    )
                    randomMedia?.let {
                        getRandomMediaLogos(it)
                    }
                    _homeState.update {
                        it.copy(
                            loading = false,
                            trending = result,
                            randomMedia = randomMedia,
                            randomMediaDominantColors = randomMediaDominantColors
                        )
                    }
                }
        }
    }

    private suspend fun getRandomMediaLogos(randomMedia: MediaInfo) {
        detailRepository.getImages(
            path = C.MEDIA_IMAGES.format(randomMedia.mediaType?.name?.lowercase().toString(), randomMedia.id),
            includeImageLanguage = "${randomMedia.originalLanguage},en,null"
        ).onSuccess { result ->
            _homeState.update { it.copy(randomMediaLogos = result.logos) }
        }
    }

    private fun getNowPlaying(language: String? = null) {
        viewModelScope.launch {
            homeRepository.getNowPlaying(language)
                .onError { error ->
                    _homeState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUiText()
                        )
                    }
                }
                .onSuccess { result ->
                    _homeState.update {
                        it.copy(
                            loading = false,
                            nowPlaying = result
                        )
                    }
                }
        }
    }

    fun homeUiEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnNavigateTo -> Unit
            HomeUiEvent.ChangeRandom -> {
                viewModelScope.launch {
                    val randomMedia = homeState.value.trending?.results?.randomOrNull()
                    val randomMediaDominantColors = dominantColorRepository.calculateDominantColor(
                        imageUrl = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W780 + randomMedia?.backdropPath
                    )
                    _homeState.update {
                        it.copy(
                            randomMedia = randomMedia,
                            randomMediaDominantColors = randomMediaDominantColors
                        )
                    }
                }
            }
        }
    }
}