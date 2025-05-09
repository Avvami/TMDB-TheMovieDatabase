package com.personal.tmdb.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.repository.DominantColorRepository
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.repository.UserRepository
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.TimeWindow
import com.personal.tmdb.core.domain.util.fold
import com.personal.tmdb.core.domain.util.toUiText
import com.personal.tmdb.detail.data.models.Image
import com.personal.tmdb.detail.domain.repository.DetailRepository
import com.personal.tmdb.home.domain.models.TrendingResult
import com.personal.tmdb.home.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
    private val homeRepository: HomeRepository,
    private val detailRepository: DetailRepository,
    private val dominantColorRepository: DominantColorRepository
): ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _homeState.update {
                it.copy(
                    loading = true,
                    errorMessage = null
                )
            }

            val language = preferencesRepository.getLanguage()
            val region = userRepository.getUser()?.iso31661 ?: "US"

            val randomMediaResultDeferred = async { getRandomTrendingMedia(TimeWindow.DAY, language) }

            val trending = homeRepository.getTrending(TimeWindow.DAY, language).flow.cachedIn(viewModelScope)
            val popularMovies = homeRepository.getPopular(MediaType.MOVIE, language).flow.cachedIn(viewModelScope)
            val popularTvShows = homeRepository.getPopular(MediaType.TV, language).flow.cachedIn(viewModelScope)
            val upcomingMovies = homeRepository.getUpcomingMovies(language, region).flow.cachedIn(viewModelScope)

            val randomMediaResult = randomMediaResultDeferred.await()

            _homeState.update {
                it.copy(
                    loading = false,
                    randomMedia = randomMediaResult.randomMedia,
                    randomMediaDominantColors = randomMediaResult.randomMediaDominantColors,
                    randomMediaLogos = randomMediaResult.randomMediaLogos,
                    trending = trending,
                    popularMovies = popularMovies,
                    popularTvShows = popularTvShows,
                    upcomingMovies = upcomingMovies,
                    errorMessage = randomMediaResult.errorMessage
                )
            }
        }
    }

    private suspend fun getRandomTrendingMedia(timeWindow: TimeWindow, language: String): TrendingResult = coroutineScope {
        homeRepository.getTrendingPage(timeWindow, language)
            .fold(
                onSuccess = { result ->
                    val randomMedia = result.results.randomOrNull()
                    val randomMediaDominantColors = async {
                        dominantColorRepository.calculateDominantColor(
                            imageUrl = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W780 + randomMedia?.backdropPath
                        )
                    }
                    val randomMediaLogos = async { randomMedia?.let { getRandomMediaLogos(it, language) } }
                    TrendingResult(
                        randomMedia = randomMedia,
                        randomMediaDominantColors = randomMediaDominantColors.await(),
                        randomMediaLogos = randomMediaLogos.await()
                    )
                },
                onError = { error ->
                    TrendingResult(errorMessage = error.toUiText())
                }
            )
    }

    private suspend fun getRandomMediaLogos(randomMedia: MediaInfo, language: String): List<Image?> {
        return detailRepository.getImages(
            path = C.MEDIA_IMAGES.format(randomMedia.mediaType?.name?.lowercase().toString(), randomMedia.id),
            includeImageLanguage = "${randomMedia.originalLanguage},$language,en"
        ).fold(
            onError = {
                emptyList()
            },
            onSuccess = { result ->
                result.logos ?: emptyList()
            }
        )
    }

    fun homeUiEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnNavigateTo -> Unit
            HomeUiEvent.RetryRequests -> { loadData() }
        }
    }
}