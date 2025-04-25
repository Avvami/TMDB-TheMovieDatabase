package com.personal.tmdb.detail.presentation.cast

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.detail.presentation.cast.components.AnnotatedCastItem
import com.personal.tmdb.detail.presentation.cast.components.CastInfoCard
import com.personal.tmdb.detail.presentation.cast.components.CastScreenShimmer

@Composable
fun CastScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    viewModel: CastViewModel = hiltViewModel()
) {
    val castState by viewModel.castState.collectAsStateWithLifecycle()
    CastScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        castState = { castState },
        castUiEvent = { event ->
            when (event) {
                CastUiEvent.OnNavigateBack -> onNavigateBack()
                is CastUiEvent.OnNavigateTo -> onNavigateTo(event.route)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CastScreen(
    modifier: Modifier = Modifier,
    castState: () -> CastState,
    castUiEvent: (CastUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = castState().mediaName + (castState().seasonNumber?.let { season ->
                            castState().episodeNumber?.let { episode ->
                                " ${stringResource(id = R.string.season_episode, season, episode)}"
                            }
                        } ?: ""),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { castUiEvent(CastUiEvent.OnNavigateBack) },
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
                            if (castState().episodeNumber != null && castState().seasonNumber != null) {
                                context.shareText(
                                    C.SHARE_EPISODE_CAST.format(
                                        castState().mediaId,
                                        castState().seasonNumber,
                                        castState().episodeNumber
                                    )
                                )
                            } else {
                                context.shareText(
                                    C.SHARE_CAST.format(
                                        castState().mediaType.name.lowercase(),
                                        castState().mediaId
                                    )
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            if (castState().loading) {
                item {
                    CastScreenShimmer()
                }
            } else {
                castState().credits?.let { credits ->
                    if (!credits.guestStars.isNullOrEmpty()) {
                        stickyHeader(
                            contentType = "mainHeader"
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                text = stringResource(id = R.string.guest_stars),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        items(credits.guestStars) { cast ->
                            CastInfoCard(
                                onNavigateTo = { castUiEvent(CastUiEvent.OnNavigateTo(it)) },
                                profileId = cast.id,
                                profilePath = cast.profilePath,
                                name = cast.name,
                                character = cast.character,
                                activity = cast.roles?.map { AnnotatedCastItem(it.creditId, it.character, it.episodeCount) }
                            )
                        }
                    }
                    if (!credits.cast.isNullOrEmpty()) {
                        stickyHeader(
                            contentType = "mainHeader"
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                text = stringResource(id = R.string.cast),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        items(credits.cast) { cast ->
                            CastInfoCard(
                                onNavigateTo = { castUiEvent(CastUiEvent.OnNavigateTo(it)) },
                                profileId = cast.id,
                                profilePath = cast.profilePath,
                                name = cast.name,
                                character = cast.character,
                                activity = cast.roles?.map { AnnotatedCastItem(it.creditId, it.character, it.episodeCount) }
                            )
                        }
                    }
                    if (!credits.crew.isNullOrEmpty()) {
                        stickyHeader(
                            contentType = "mainHeader"
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                text = stringResource(id = R.string.crew),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        credits.crew.forEach { (department, crewList) ->
                            if (!department.isNullOrEmpty()) {
                                stickyHeader(
                                    contentType = "subHeader"
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                            .padding(horizontal = 16.dp, vertical = 4.dp),
                                        text = department,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                            if (!crewList.isNullOrEmpty()) {
                                items(crewList) { crew ->
                                    CastInfoCard(
                                        onNavigateTo = { castUiEvent(CastUiEvent.OnNavigateTo(it)) },
                                        profileId = crew.id,
                                        profilePath = crew.profilePath,
                                        name = crew.name,
                                        job = crew.job,
                                        activity = crew.jobs?.map { AnnotatedCastItem(it.creditId, it.job, it.episodeCount) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}