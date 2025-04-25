package com.personal.tmdb.home.presentation.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.personal.tmdb.core.domain.util.convertMediaType
import com.personal.tmdb.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val routeData = savedStateHandle.toRoute<Route.Discover>()

    private val _discoverState = MutableStateFlow(
        DiscoverState(
            uiState = convertMediaType(routeData.mediaType)
        )
    )
    val discoverState = _discoverState.asStateFlow()
}