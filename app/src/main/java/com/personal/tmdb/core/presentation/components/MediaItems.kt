package com.personal.tmdb.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.MyList
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.formatDateTime
import com.personal.tmdb.core.domain.util.formatListVisibility
import com.personal.tmdb.core.domain.util.formatNumberOfItems
import com.personal.tmdb.core.domain.util.formatVoteAverage
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.ui.theme.onSurfaceDark
import com.personal.tmdb.ui.theme.onSurfaceLight
import com.personal.tmdb.ui.theme.surfaceDark
import com.personal.tmdb.ui.theme.surfaceLight
import com.personal.tmdb.ui.theme.surfaceVariantDark

@Composable
fun MediaBanner(
    modifier: Modifier = Modifier,
    onNavigateTo: (route: Route) -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    height: Dp = 180.dp,
    mediaInfo: MediaInfo,
    mediaType: MediaType?,
    showVoteAverage: Boolean
) {
    Box(
        modifier = modifier
            .height(height)
            .aspectRatio(16 / 9f)
            .clip(shape)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(.1f),
                shape = shape
            )
            .clickable {
                mediaType?.let { mediaType ->
                    when (mediaType) {
                        MediaType.TV, MediaType.MOVIE -> {
                            onNavigateTo(
                                Route.Detail(
                                    mediaType = mediaType.name.lowercase(),
                                    mediaId = mediaInfo.id
                                )
                            )
                        }

                        MediaType.PERSON -> {
                            onNavigateTo(
                                Route.Person(
                                    personName = mediaInfo.name ?: "",
                                    personId = mediaInfo.id
                                )
                            )
                        }

                        else -> {
                            onNavigateTo(Route.Lost)
                        }
                    }
                }
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .matchParentSize(),
            model = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W780 + mediaInfo.backdropPath,
            contentDescription = "Backdrop",
            placeholder = painterResource(id = R.drawable.placeholder),
            error = painterResource(id = R.drawable.placeholder),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(onSurfaceLight.copy(.6f))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(2 / 3f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = C.TMDB_IMAGES_BASE_URL + C.POSTER_W300 + mediaInfo.posterPath,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Poster",
                    contentScale = ContentScale.Crop
                )
                if (showVoteAverage && mediaInfo.voteAverage != null) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(onSurfaceLight.copy(alpha = .5f))
                            .padding(horizontal = 4.dp)
                            .align(Alignment.TopStart),
                        text = formatVoteAverage(mediaInfo.voteAverage),
                        style = MaterialTheme.typography.labelMedium,
                        color = surfaceLight
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                mediaInfo.name?.let { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        color = surfaceLight,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                mediaInfo.overview?.let { overview ->
                    Text(
                        text = overview,
                        style = MaterialTheme.typography.bodySmall,
                        color = surfaceLight,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun MediaBannerShimmer(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    height: Dp = 180.dp,
) {
    Box(
        modifier = modifier
            .height(height)
            .aspectRatio(16 / 9f)
            .clip(shape)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(.1f),
                shape = shape
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(2 / 3f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    text = "Title",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Transparent
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    text = "",
                    style = MaterialTheme.typography.bodySmall,
                    minLines = 3
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPoster(
    modifier: Modifier = Modifier,
    onNavigateTo: (route: Route) -> Unit,
    selectEnabled: () -> Boolean = { false },
    multipleSelectEnabled: Boolean = true,
    selected: () -> Boolean = { false },
    onSelect: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.medium,
    height: Dp = 200.dp,
    mediaInfo: MediaInfo,
    mediaType: MediaType?,
    showTitle: Boolean,
    showVoteAverage: Boolean
) {
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .height(height)
                .aspectRatio(2 / 3f)
                .clip(shape)
                .then(
                    if (!selectEnabled()) {
                        Modifier.combinedClickable(
                            onClick = {
                                when (mediaType) {
                                    MediaType.TV, MediaType.MOVIE -> {
                                        onNavigateTo(
                                            Route.Detail(
                                                mediaType = mediaType.name.lowercase(),
                                                mediaId = mediaInfo.id
                                            )
                                        )
                                    }
                                    MediaType.PERSON -> {
                                        onNavigateTo(
                                            Route.Person(
                                                personName = mediaInfo.name ?: "",
                                                personId = mediaInfo.id
                                            )
                                        )
                                    }
                                    else -> {
                                        onNavigateTo(Route.Lost)
                                    }
                                }
                            },
                            onLongClick = {
                                onLongClick?.let {
                                    it()
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            }
                        )
                    } else {
                        Modifier.selectable(selected = selected(), onClick = onSelect)
                    }
                )
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = C.TMDB_IMAGES_BASE_URL + C.POSTER_W300 + mediaInfo.posterPath,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
                contentDescription = "Poster",
                contentScale = ContentScale.Crop
            )
            if (showVoteAverage && mediaInfo.voteAverage != null) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(onSurfaceLight.copy(alpha = .5f))
                        .padding(horizontal = 4.dp)
                        .align(Alignment.TopStart),
                    text = formatVoteAverage(mediaInfo.voteAverage),
                    style = MaterialTheme.typography.labelMedium,
                    color = surfaceLight
                )
            }
            if (!showTitle && mediaType?.equals(MediaType.PERSON) == true) {
                mediaInfo.name?.let { name ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(onSurfaceLight.copy(alpha = .5f))
                            .padding(4.dp)
                            .align(Alignment.BottomCenter),
                        text = name,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                            lineHeight = MaterialTheme.typography.labelSmall.lineHeight,
                            letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing,
                            color = surfaceLight,
                            shadow = Shadow(color = MaterialTheme.colorScheme.scrim.copy(alpha = .5f), offset = Offset(1f, 1f), blurRadius = 2f)
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = selectEnabled()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(surfaceDark.copy(alpha = .65f)),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = selected(),
                        label = "Selected animation"
                    ) { selected ->
                        when {
                            selected -> {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = surfaceLight
                                )
                            }
                            multipleSelectEnabled && !selected -> {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    painter = painterResource(id = R.drawable.icon_radio_button_unchecked_fill0_wght400),
                                    contentDescription = null,
                                    tint = surfaceLight
                                )
                            }
                        }
                    }
                }
            }
        }
        if (showTitle) {
            mediaInfo.name?.let { name ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun MediaPosterShimmer(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    height: Dp = 200.dp,
    showTitle: Boolean
) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .height(height)
                .aspectRatio(2 / 3f)
                .clip(shape)
                .shimmerEffect()
        )
        if (showTitle) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "",
                minLines = 2,
                maxLines = 2,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    onNavigateTo: (route: Route) -> Unit,
    selectEnabled: () -> Boolean = { false },
    multipleSelectEnabled: Boolean = true,
    selected: () -> Boolean = { false },
    onSelect: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    height: Dp = 200.dp,
    myList: MyList
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .height(height)
            .aspectRatio(16 / 9f)
            .clip(shape)
            .then(
                if (!selectEnabled()) {
                    Modifier.combinedClickable(
                        onClick = {
                            onNavigateTo(
                                Route.ListDetails(listId = myList.id)
                            )
                        },
                        onLongClick = {
                            onLongClick?.let {
                                it()
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                    )
                } else {
                    Modifier.selectable(selected = selected(), onClick = onSelect)
                }
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                shape = shape
            )
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W780 + myList.backdropPath,
            placeholder = painterResource(id = R.drawable.placeholder),
            error = painterResource(id = R.drawable.placeholder),
            contentDescription = "Poster",
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.scrim.copy(.6f), BlendMode.Darken)
        )
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
            LocalContentColor provides onSurfaceDark
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                myList.name?.let { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatNumberOfItems(myList.numberOfItems),
                        style = MaterialTheme.typography.labelLarge
                    )
                    SuggestionChip(
                        enabled = false,
                        onClick = {},
                        label = {
                            Text(text = formatListVisibility(myList.public).asString().uppercase())
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            disabledLabelColor = onSurfaceDark,
                            disabledContainerColor = onSurfaceDark.copy(alpha = .1f)
                        ),
                        border = null
                    )
                }
                myList.updatedAt?.let { updatedAt ->
                    Text(
                        text = formatDateTime(updatedAt).asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = surfaceVariantDark
                    )
                }
            }
        }
        androidx.compose.animation.AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = selectEnabled()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceDark.copy(alpha = .65f)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = selected(),
                    label = "Selected animation"
                ) { selected ->
                    when {
                        selected -> {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = surfaceLight
                            )
                        }
                        multipleSelectEnabled && !selected -> {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(id = R.drawable.icon_radio_button_unchecked_fill0_wght400),
                                contentDescription = null,
                                tint = surfaceLight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItemShimmer(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    height: Dp = 200.dp
) {
    Box(
        modifier = modifier
            .height(height)
            .aspectRatio(16 / 9f)
            .clip(shape)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                shape = shape
            )
    ) {
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
            LocalContentColor provides Color.Transparent
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    text = "List name",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect(),
                        text = "00 items",
                        style = MaterialTheme.typography.labelLarge
                    )
                    SuggestionChip(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .shimmerEffect(),
                        enabled = false,
                        onClick = {},
                        label = {
                            Text(text = "PUBLIC")
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            disabledLabelColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        border = null
                    )
                }
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    text = "Updated at 00.00.0000",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}