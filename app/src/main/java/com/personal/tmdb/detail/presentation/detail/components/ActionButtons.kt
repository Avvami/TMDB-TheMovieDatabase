package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.getColorForVoteAverage
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.detail.data.models.Rated
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.onSurfaceDark
import com.personal.tmdb.ui.theme.surfaceContainerDark
import java.time.LocalDate

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    userState: () -> UserState,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    /*TODO: Change buttons size with material expressive*/
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
    ) {
        FlowRow(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!userState().user?.sessionId.isNullOrEmpty() &&
                detailState.details?.releaseDate?.isBefore(LocalDate.now()) == true) {
                Button(
                    modifier = Modifier.defaultMinSize(
                        minWidth = 56.dp,
                        minHeight = 56.dp
                    ),
                    onClick = { detailUiEvent(DetailUiEvent.ShowRatingSheet(true)) },
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (val rated = detailState.accountState?.rated) {
                            is Rated.Value -> getColorForVoteAverage(rated.value.toFloat())
                            else -> surfaceContainerDark
                        },
                        contentColor = onSurfaceDark
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = surfaceContainerDark
                    )
                ) {
                    when (val rated = detailState.accountState?.rated) {
                        is Rated.Value -> {
                            when {
                                rated.value < 5 -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_thumb_down_fill1_wght400),
                                        contentDescription = null
                                    )
                                }
                                rated.value < 7 -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                                        contentDescription = null
                                    )
                                }
                                else -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_thumb_up_fill1_wght400),
                                        contentDescription = null
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text(
                                text = rated.value.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        else -> {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill0_wght400),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            if (!userState().user?.sessionId.isNullOrEmpty()) {
                OutlinedIconButton(
                    modifier = Modifier
                        .size(56.dp)
                        .then(
                            if (detailState.watchlistLoadState is LoadState.Loading) {
                                Modifier
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            } else Modifier
                        ),
                    onClick = {
                        if (detailState.watchlistLoadState is LoadState.Loading)
                            return@OutlinedIconButton
                        detailState.accountState?.let { accountState ->
                            detailUiEvent(DetailUiEvent.AddToWatchlist(!accountState.watchlist))
                        }
                    },
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        containerColor = if (detailState.watchlistLoadState is LoadState.Loading) {
                            Color.Transparent
                        } else {
                            surfaceContainerDark
                        }
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = surfaceContainerDark
                    )
                ) {
                    AnimatedContent(
                        targetState = detailState.accountState?.watchlist == true,
                        transitionSpec = {
                            if (targetState) {
                                fadeIn(animationSpec = spring()) + scaleIn(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    ),
                                    initialScale = .4f
                                ) togetherWith ExitTransition.None
                            } else {
                                EnterTransition.None togetherWith ExitTransition.None
                            }
                        }
                    ) { inWatchlist ->
                        if (inWatchlist) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_bookmarks_fill1_wght400),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_bookmarks_fill0_wght400),
                                contentDescription = null,
                                tint = onSurfaceDark
                            )
                        }
                    }
                }
            }
            OutlinedIconButton(
                modifier = Modifier.size(56.dp),
                onClick = { detailUiEvent(DetailUiEvent.Share) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = surfaceContainerDark,
                    contentColor = onSurfaceDark
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = surfaceContainerDark
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_share_fill0_wght400),
                    contentDescription = null
                )
            }
            OutlinedIconButton(
                modifier = Modifier.size(56.dp),
                onClick = { detailUiEvent(DetailUiEvent.ShowMoreSheet(true)) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = surfaceContainerDark,
                    contentColor = onSurfaceDark
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = surfaceContainerDark
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_more_horiz_fill0_wght400),
                    contentDescription = null
                )
            }
        }
    }
}