package com.personal.tmdb.detail.presentation.person

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.core.presentation.components.MediaPoster
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.detail.presentation.person.components.PersonBio
import com.personal.tmdb.detail.presentation.person.components.PersonCreditsSorting
import com.personal.tmdb.detail.presentation.person.components.PersonExternalIdsRow
import com.personal.tmdb.detail.presentation.person.components.PersonInfo
import com.personal.tmdb.detail.presentation.person.components.PersonKnownForMedia
import com.personal.tmdb.detail.presentation.person.components.PersonPhotoCarousel
import com.personal.tmdb.detail.presentation.person.components.PersonScreenShimmer

@Composable
fun PersonScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: Route) -> Unit,
    preferencesState: () -> PreferencesState,
    viewModel: PersonViewModel = hiltViewModel()
) {
    val personState by viewModel.personState.collectAsStateWithLifecycle()
    val personCreditsState by viewModel.personCreditsState.collectAsStateWithLifecycle()
    PersonScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        preferencesState = preferencesState,
        personState = { personState },
        personCreditsState = { personCreditsState },
        personUiEvent = { event ->
            when (event) {
                PersonUiEvent.OnNavigateBack -> onNavigateBack()
                is PersonUiEvent.OnNavigateTo -> onNavigateTo(event.route)
                else -> Unit
            }
            viewModel.personUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PersonScreen(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    personState: () -> PersonState,
    personCreditsState: () -> PersonCreditsState,
    personUiEvent: (PersonUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SelectionContainer {
                        Text(
                            text = personState().personName,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { personUiEvent(PersonUiEvent.OnNavigateBack) }
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
                            context.shareText(C.SHARE_PERSON.format(personState().personId))
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
            contentPadding = PaddingValues(top = 8.dp, bottom = 12.dp)
        ) {
            if (personState().loading) {
                item {
                    CompositionLocalProvider(
                        LocalContentColor provides Color.Transparent
                    ) {
                        PersonScreenShimmer(
                            preferencesState = preferencesState
                        )
                    }
                }
            } else {
                personState().personInfo?.let { personInfo ->
                    item {
                        PersonPhotoCarousel(
                            modifier = Modifier.fillMaxWidth(),
                            onNavigateTo = { personUiEvent(PersonUiEvent.OnNavigateTo(it)) },
                            personInfo = { personInfo }
                        )
                    }
                    personInfo.externalIds?.let { externalIds ->
                        item {
                            PersonExternalIdsRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(start = 16.dp, top = 4.dp, end = 16.dp),
                                externalIds = { externalIds }
                            )
                        }
                    }
                    item {
                        PersonInfo(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 4.dp, end = 16.dp),
                            personInfo = { personInfo }
                        )
                    }
                    item {
                        PersonBio(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            personInfo = { personInfo }
                        )
                    }
                    personInfo.combinedCreditsInfo?.let { combinedCredits ->
                        item {
                            MediaCarousel(
                                modifier = Modifier
                                    .animateItem()
                                    .padding(top = 16.dp),
                                titleContent = {
                                    Text(
                                        text = stringResource(
                                            id = if ((combinedCredits.castMediaInfo?.size ?: 0) > 2) R.string.person_starring else R.string.known_for
                                        )
                                    )
                                },
                                items = {
                                    if ((combinedCredits.castMediaInfo?.size ?: 0) > 2) {
                                        combinedCredits.castMediaInfo?.let { castMediaInfo ->
                                            items(
                                                items = castMediaInfo,
                                                key = { it.id }
                                            ) { mediaInfo ->
                                                MediaPoster(
                                                    onNavigateTo = { personUiEvent(PersonUiEvent.OnNavigateTo(it)) },
                                                    mediaInfo = mediaInfo,
                                                    mediaType = mediaInfo.mediaType,
                                                    showTitle = preferencesState().showTitle,
                                                    showVoteAverage = preferencesState().showVoteAverage,
                                                )
                                            }
                                        }
                                    } else {
                                        combinedCredits.crewMediaInfo?.let { crewMediaInfo ->
                                            items(
                                                items = crewMediaInfo,
                                                key = { it.id }
                                            ) { mediaInfo ->
                                                MediaPoster(
                                                    onNavigateTo = { personUiEvent(PersonUiEvent.OnNavigateTo(it)) },
                                                    mediaInfo = mediaInfo,
                                                    mediaType = mediaInfo.mediaType,
                                                    showTitle = preferencesState().showTitle,
                                                    showVoteAverage = preferencesState().showVoteAverage,
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                    personCreditsState().filteredPersonCredits?.let { personCredits ->
                        item {
                            PersonCreditsSorting(
                                modifier = Modifier
                                    .animateItem()
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
                                personCreditsState = personCreditsState,
                                personUiEvent = personUiEvent
                            )
                        }
                        personCredits.forEach { (department, knownFor) ->
                            if (knownFor?.values?.any { it.isNotEmpty() } == true) {
                                stickyHeader {
                                    Text(
                                        modifier = Modifier
                                            .animateItem()
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                            .padding(horizontal = 16.dp, vertical = 4.dp),
                                        text = department ?: stringResource(id = R.string.acting),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            knownFor?.values?.forEachIndexed { groupIndex, infos ->
                                itemsIndexed(
                                    items = infos,
                                    key = { _, item -> item.uniqueId }
                                ) { index, info ->
                                    Column(
                                        modifier = Modifier.animateItem()
                                    ) {
                                        PersonKnownForMedia(
                                            modifier = Modifier
                                                .animateItem()
                                                .fillMaxWidth(),
                                            onNavigateTo = { personUiEvent(PersonUiEvent.OnNavigateTo(it)) },
                                            info = { info }
                                        )
                                        if (index == infos.lastIndex && knownFor.values.size - 1 != groupIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.clip(MaterialTheme.shapes.extraLarge),
                                                thickness = 2.dp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f)
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
    }
}