package com.personal.tmdb.home.presentation.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.AutoResizedText
import com.personal.tmdb.home.presentation.home.HomeState
import com.personal.tmdb.home.presentation.home.HomeUiEvent
import com.personal.tmdb.ui.theme.surfaceContainerDark
import com.personal.tmdb.ui.theme.surfaceLight

@Composable
fun HomeBanner(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    homeState: () -> HomeState,
    homeUiEvent: (HomeUiEvent) -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                homeState().randomMedia?.let { randomMedia ->
                    when (randomMedia.mediaType) {
                        MediaType.TV, MediaType.MOVIE -> {
                            homeUiEvent(
                                HomeUiEvent.OnNavigateTo(
                                    Route.Detail(
                                        mediaType = randomMedia.mediaType.name.lowercase(),
                                        mediaId = randomMedia.id
                                    )
                                )
                            )
                        }
                        MediaType.PERSON -> {
                            homeUiEvent(
                                HomeUiEvent.OnNavigateTo(
                                    Route.Person(
                                        personName = randomMedia.mediaType.name,
                                        personId = randomMedia.id
                                    )
                                )
                            )
                        }
                        else -> {
                            homeUiEvent(HomeUiEvent.OnNavigateTo(Route.Lost))
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        var showGradient by rememberSaveable { mutableStateOf(false) }
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W1280 + homeState().randomMedia?.backdropPath,
            contentDescription = "Backdrop",
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.scrim.copy(.1f), BlendMode.Darken),
            onSuccess = { showGradient = true }
        )
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = showGradient,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, surfaceContainerDark.copy(alpha = .65f)),
                            startY = 500f
                        )
                    )
            )
        }
        homeState().randomMediaLogos?.let { logos ->
            val painter = rememberAsyncImagePainter(
                model = C.TMDB_IMAGES_BASE_URL + C.LOGO_W500 + (logos.find { it?.iso6391 == preferencesState().language }?.filePath
                    ?: homeState().randomMediaLogos?.getOrNull(0)?.filePath)
            )
            val painterState by painter.state.collectAsStateWithLifecycle()
            when (painterState) {
                is AsyncImagePainter.State.Success -> {
                    Image(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                            .sizeIn(maxWidth = 300.dp, maxHeight = 85.dp)
                            .fillMaxSize(),
                        painter = painter,
                        contentDescription = "Logo",
                        contentScale = ContentScale.Inside
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    AutoResizedText(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        text = homeState().randomMedia?.name ?: "",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = surfaceLight
                    )
                }
                else -> Unit
            }
        }
    }
}