package com.personal.tmdb.detail.presentation.episode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.detail.domain.util.ImageType
import com.personal.tmdb.detail.presentation.detail.components.AnnotatedItem
import com.personal.tmdb.detail.presentation.detail.components.AnnotatedOverflowListText
import com.personal.tmdb.detail.presentation.episode.components.EpisodeActionButtons
import com.personal.tmdb.detail.presentation.episode.components.EpisodeDetailsShimmer
import com.personal.tmdb.detail.presentation.episode.components.EpisodeOverview
import com.personal.tmdb.detail.presentation.episode.components.EpisodeTitle

@Composable
fun EpisodeDetailsScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    userState: () -> UserState,
    viewModel: EpisodeDetailsViewModel = hiltViewModel()
) {
    val episodeDetailsState by viewModel.episodeDetailsState.collectAsStateWithLifecycle()
    EpisodeDetailsScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        episodeDetailsState = { episodeDetailsState },
        userState = userState,
        episodeDetailsUiEvent = { event ->
            when (event) {
                EpisodeDetailsUiEvent.OnNavigateBack -> onNavigateBack()
                is EpisodeDetailsUiEvent.OnNavigateTo -> onNavigateTo(event.route)
            }
            viewModel.episodeDetailsUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodeDetailsScreen(
    modifier: Modifier = Modifier,
    episodeDetailsState: () -> EpisodeDetailsState,
    userState: () -> UserState,
    episodeDetailsUiEvent: (EpisodeDetailsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.season_episode,
                            episodeDetailsState().seasonNumber,
                            episodeDetailsState().episodeNumber
                        ),
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            episodeDetailsUiEvent(EpisodeDetailsUiEvent.OnNavigateBack)
                        }
                    )  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    val context = LocalContext.current
                    IconButton(
                        onClick = {
                            context.shareText(
                                C.SHARE_EPISODE.format(
                                    episodeDetailsState().mediaId,
                                    episodeDetailsState().seasonNumber,
                                    episodeDetailsState().episodeNumber
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
        ) {
            if (episodeDetailsState().loading) {
                item {
                    EpisodeDetailsShimmer(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        userState = userState
                    )
                }
            } else {
                episodeDetailsState().episodeDetails?.let { episodeDetails ->
                    item {
                        EpisodeTitle(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            episodeInfo = { episodeDetails }
                        )
                    }
                    episodeDetails.overview?.let { overview ->
                        item {
                            EpisodeOverview(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                overview = overview
                            )
                        }
                    }
                    episodeDetails.guestStars?.takeIf { it.isNotEmpty() }?.let { guestStars ->
                        item {
                            CompositionLocalProvider(
                                LocalContentColor provides MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                AnnotatedOverflowListText(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .clickable(
                                            interactionSource = null,
                                            indication = null
                                        ) {
                                            episodeDetailsUiEvent(
                                                EpisodeDetailsUiEvent.OnNavigateTo(
                                                    Route.Cast(
                                                        mediaName = episodeDetails.name ?: "",
                                                        mediaType = MediaType.TV.name.lowercase(),
                                                        mediaId = episodeDetailsState().mediaId,
                                                        seasonNumber = episodeDetailsState().seasonNumber,
                                                        episodeNumber = episodeDetailsState().episodeNumber
                                                    )
                                                )
                                            )
                                        },
                                    titlePrefix = stringResource(id = R.string.guest_stars_list),
                                    items = guestStars.map { AnnotatedItem(id = it.id, name = it.name ?: "") }
                                )
                            }
                        }
                    }
                    if (!userState().user?.sessionId.isNullOrEmpty() || episodeDetails.voteAverage != null) {
                        item {
                            EpisodeActionButtons(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                episodeDetails = { episodeDetails },
                                userState = userState
                            )
                        }
                    }
                    episodeDetails.images?.stills?.takeIf { it.isNotEmpty() }?.let { stills ->
                        item {
                            MediaCarousel(
                                titleContent = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .then(
                                                if (stills.size > 1) {
                                                    Modifier.clickable(
                                                        interactionSource = null,
                                                        indication = null
                                                    ) {
                                                        episodeDetailsUiEvent(
                                                            EpisodeDetailsUiEvent.OnNavigateTo(
                                                                Route.Images(
                                                                    imageType = ImageType.STILLS.name.lowercase(),
                                                                    imagesPath = C.EPISODE_MEDIA_IMAGES.format(
                                                                        episodeDetailsState().mediaId,
                                                                        episodeDetailsState().seasonNumber,
                                                                        episodeDetailsState().episodeNumber
                                                                    )
                                                                )
                                                            )
                                                        )
                                                    }
                                                } else {
                                                    Modifier
                                                }
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            text = stringResource(id = R.string.stills)
                                        )
                                        if (stills.size > 1) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                },
                                items = {
                                    itemsIndexed(
                                        items = stills
                                    ) { index, image ->
                                        AsyncImage(
                                            modifier = Modifier
                                                .height(170.dp)
                                                .aspectRatio(16 / 9f)
                                                .clip(MaterialTheme.shapes.medium)
                                                .clickable {
                                                    episodeDetailsUiEvent(
                                                        EpisodeDetailsUiEvent.OnNavigateTo(
                                                            Route.Images(
                                                                imageType = ImageType.STILLS.name.lowercase(),
                                                                imagesPath = C.EPISODE_MEDIA_IMAGES.format(
                                                                    episodeDetailsState().mediaId,
                                                                    episodeDetailsState().seasonNumber,
                                                                    episodeDetailsState().episodeNumber
                                                                ),
                                                                selectedImageIndex = index
                                                            )
                                                        )
                                                    )
                                                },
                                            model = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W780 + image?.filePath,
                                            placeholder = painterResource(id = R.drawable.placeholder),
                                            error = painterResource(id = R.drawable.placeholder),
                                            contentDescription = "Poster",
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}