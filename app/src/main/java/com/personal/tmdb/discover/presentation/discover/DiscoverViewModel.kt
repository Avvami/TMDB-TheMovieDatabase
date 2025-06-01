package com.personal.tmdb.discover.presentation.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.convertMediaType
import com.personal.tmdb.core.domain.util.fold
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.domain.models.GenresInfo
import com.personal.tmdb.discover.domain.repository.DiscoverRepository
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferencesRepository: PreferencesRepository,
    private val discoverRepository: DiscoverRepository
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.Discover>()

    private val _discoverState = MutableStateFlow(
        DiscoverState(
            uiState = convertMediaType(routeData.mediaType)
        )
    )
    val discoverState = _discoverState.asStateFlow()

    private val _filtersState = MutableStateFlow(FiltersState())
    val filtersState = _filtersState.asStateFlow()

    init {
        discover(
            mediaType = routeData.mediaType,
            filters = _filtersState.value
        )
    }

    private fun discover(
        mediaType: String,
        filters: FiltersState
    ) {
        viewModelScope.launch {
            _discoverState.update {
                it.copy(loading = true)
            }
            val language = preferencesRepository.getLanguage()

            if (convertMediaType(mediaType) == MediaType.PERSON) {
                val discover =
                    discoverRepository.getPopularPeople(language).flow.cachedIn(viewModelScope)
                _discoverState.update {
                    it.copy(
                        loading = false,
                        discover = discover
                    )
                }
            } else {
                val genresDeferred = if (_discoverState.value.genresInfo == null) {
                    async { getGenres(mediaType, language) }
                } else {
                    null
                }
                val discover =
                    discoverMedia(mediaType, language, filters).flow.cachedIn(viewModelScope)
                _discoverState.update {
                    it.copy(
                        loading = false,
                        discover = discover,
                        genresInfo = genresDeferred?.await() ?: _discoverState.value.genresInfo
                    )
                }
            }
        }
    }

    private suspend fun discoverMedia(
        mediaType: String,
        language: String?,
        filters: FiltersState
    ): Pager<Int, MediaInfo> {
        return discoverRepository.discoverMedia(
            mediaType = mediaType,
            language = language,
            includeAdult = filters.includeAdult,
            airDateYear = "",
            fromAirDate = "",
            toAirDate = "",
            sortBy = "",
            fromRating = 0f,
            toRating = 0f,
            minRatingCount = filters.minimumVoteCount.takeIf { it.isNotEmpty() }?.toFloatOrNull()
                ?: filters.minimumVoteCountDefault.toFloat(),
            withGenre = _discoverState.value.selectedGenre?.id?.toString() ?: "",
            withOriginCountry = "",
            withOriginalLanguage = "",
            fromRuntime = filters.startRuntimeDefault,
            toRuntime = filters.endRuntimeDefault
        )
    }

    private suspend fun getGenres(mediaType: String, language: String?): GenresInfo? {
        return discoverRepository.getGenres(mediaType, language).fold(
            onSuccess = { it },
            onError = { null }
        )
    }

    fun discoverUiEvent(event: DiscoverUiEvent) {
        when (event) {
            DiscoverUiEvent.OnNavigateBack -> Unit
            is DiscoverUiEvent.OnNavigateTo -> Unit
            is DiscoverUiEvent.SetShowGenresState -> {
                _discoverState.update { it.copy(showGenres = event.state) }
            }
            is DiscoverUiEvent.SetFilters -> {
                _filtersState.update { event.filters }
            }
            is DiscoverUiEvent.SetGenre -> {
                _discoverState.update { it.copy(selectedGenre = event.genre) }
                discover(
                    mediaType = routeData.mediaType,
                    filters = _filtersState.value
                )
            }
        }
    }
}