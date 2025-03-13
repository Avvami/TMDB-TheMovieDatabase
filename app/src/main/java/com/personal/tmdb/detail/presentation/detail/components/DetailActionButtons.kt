package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.compactDecimalFormat
import com.personal.tmdb.core.domain.util.formatVoteAverage
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.data.models.Rated
import com.personal.tmdb.detail.domain.models.MediaDetailInfo
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailActionButtons(
    modifier: Modifier = Modifier,
    detailState: () -> DetailState,
    info: () -> MediaDetailInfo,
    userState: () -> UserState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        FlowRow(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                Button(
                    onClick = { /*TODO*/ },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    when (val ratedState = detailState().accountState?.rated) {
                        is Rated.Value -> {
                            when (ratedState.value) {
                                in 1..4 -> {
                                    Icon(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        painter = painterResource(id = R.drawable.icon_thumb_down_fill1_wght400),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                5 -> {
                                    Icon(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                else -> {
                                    Icon(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        painter = painterResource(id = R.drawable.icon_thumb_up_fill1_wght400),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text(text = ratedState.value.toString())
                        }
                        else -> {
                            Icon(
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill0_wght400),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text(text = stringResource(id = R.string.rate))
                        }
                    }
                }
            }
            info().voteAverage?.let { voteAverage ->
                Button(
                    onClick = {},
                    enabled = false,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.labelLarge.toSpanStyle()
                            ) {
                                append(formatVoteAverage(voteAverage))
                            }
                            info().voteCount?.takeIf { it != 0 }?.let { voteCount ->
                                withStyle(
                                    style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.surfaceVariant).toSpanStyle()
                                ) {
                                    append(" (${compactDecimalFormat(voteCount.toLong())})")
                                }
                            }
                        }
                    )
                }
            }
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                Button(
                    onClick = {
                        detailUiEvent(
                            DetailUiEvent.OnNavigateTo(
                                route = detailState().accountState?.let {
                                    Route.AddToList(
                                        mediaType = detailState().mediaType.name.lowercase(),
                                        mediaId = detailState().mediaId,
                                        watchlist = it.watchlist,
                                        favorite = it.favorite
                                    )
                                } ?: Route.Lost
                            )
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    AnimatedContent(
                        targetState = detailState().accountState,
                        label = "In list animation"
                    ) { accountState ->
                        Row {
                            when {
                                accountState?.watchlist == true -> {
                                    Icon(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        painter = painterResource(id = R.drawable.icon_bookmarks_fill1_wght400),
                                        contentDescription = null
                                    )
                                }
                                accountState?.favorite == true -> {
                                    Icon(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        painter = painterResource(id = R.drawable.icon_favorite_fill1_wght400),
                                        contentDescription = null
                                    )
                                }
                                else -> {
                                    Icon(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = null
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            if (accountState?.favorite == true || accountState?.watchlist == true) {
                                Text(text = stringResource(id = R.string.in_list))
                            } else {
                                Text(text = stringResource(id = R.string.list))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailActionButtonsShimmer(
    userState: () -> UserState
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                Button(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .shimmerEffect(),
                    enabled = false,
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill0_wght400),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(id = R.string.rate))
                }
            }
            Button(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect(),
                enabled = false,
                onClick = {},
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                )
            ) {
                Icon(
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = "10 (10)"
                )
            }
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                Button(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .shimmerEffect(),
                    enabled = false,
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(id = R.string.list))
                }
            }
        }
    }
}