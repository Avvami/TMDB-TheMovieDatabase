package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.core.presentation.components.CustomDragHandleWithButton
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBottomSheet(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    onDismissRequest: () -> Unit,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        onDismissRequest = onDismissRequest,
        dragHandle = {
            CustomDragHandleWithButton(
                onClick = onDismissRequest,
                contentPadding = PaddingValues(
                    start = 16.dp, top = 16.dp, end = 16.dp
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
        ) {
            CustomListItem(
                modifier = Modifier
                    .then(
                        if (detailState.favoriteLoadState is LoadState.Loading) {
                            Modifier.shimmerEffect(secondaryColor = Color.Transparent)
                        } else Modifier
                    ),
                enabled = detailState.favoriteLoadState !is LoadState.Loading,
                onClick = {
                    detailState.accountState?.let { accountState ->
                        detailUiEvent(DetailUiEvent.AddToFavorites(!accountState.favorite))
                    }
                },
                leadingContent = {
                    AnimatedContent(
                        targetState = detailState.accountState?.favorite == true,
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
                    ) { inFavorites ->
                        if (inFavorites) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_favorite_fill1_wght400),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_favorite_fill0_wght400),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                },
                headlineContent = {
                    AnimatedContent(
                        targetState = detailState.accountState?.favorite,
                        transitionSpec = {
                            fadeIn(animationSpec = spring()) + slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                initialOffset = { it / 3 }
                            ) togetherWith fadeOut(animationSpec = tween(90))
                        }
                    ) { inFavorites ->
                        if (inFavorites == true) {
                            Text(text = stringResource(R.string.in_favorite))
                        } else {
                            Text(text = stringResource(R.string.favorite))
                        }
                    }
                }
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
            )
            CustomListItem(
                onClick = {
                    onDismissRequest()
                    detailUiEvent(
                        DetailUiEvent.OnNavigateTo(
                            Route.AddToList(
                                mediaType = detailState.mediaType.name.lowercase(),
                                mediaId = detailState.mediaId
                            )
                        )
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_add_fill0_wght400),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant
                    )
                },
                headlineContent = {
                    Text(text = stringResource(R.string.add_to_list_lowercase))
                }
            )
        }
    }
}