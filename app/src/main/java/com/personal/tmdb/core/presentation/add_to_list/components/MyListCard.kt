package com.personal.tmdb.core.presentation.add_to_list.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.models.MyList
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.core.presentation.add_to_list.AddToListUiEvent
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.core.presentation.components.CustomListItemDefaults

@Composable
fun MyListCard(
    modifier: Modifier = Modifier,
    myList: MyList,
    addToListUiEvent: (AddToListUiEvent) -> Unit
) {
    CustomListItem(
        modifier = modifier,
        onClick = { addToListUiEvent(AddToListUiEvent.AddToList(myList.id)) },
        enabled = myList.loadState !is LoadState.Loading,
        contentPadding = PaddingValues(8.dp),
        shape = MaterialTheme.shapes.medium,
        leadingContent = {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
                    .padding(2.dp)
                    .size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                if (myList.backdropPath == null) {
                    Icon(
                        painter = painterResource(R.drawable.icon_folder_fill0_wght400),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1 / 1f)
                            .clip(RoundedCornerShape(6.dp)),
                        model = TMDB.backdropW300(myList.backdropPath),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        headlineContent = {
            Text(
                text = myList.name ?: stringResource(id = R.string.no_name),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            AnimatedContent(
                modifier = Modifier.defaultMinSize(minWidth = 24.dp, minHeight = 24.dp),
                targetState = myList.loadState,
                transitionSpec = {
                    scaleIn(initialScale = .5f) + fadeIn() togetherWith
                            scaleOut(targetScale = .8f) + fadeOut(animationSpec = tween(90))
                },
                contentAlignment = Alignment.Center
            ) { loadState ->
                when (loadState) {
                    LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            trackColor = Color.Transparent,
                            strokeWidth = 2.dp,
                            strokeCap = StrokeCap.Round
                        )
                    }
                    LoadState.Success -> {
                        Icon(
                            painter = painterResource(R.drawable.icon_check_circle_fill1_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    is LoadState.Error -> {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_error_fill1_wght400),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    LoadState.NotLoading -> Unit
                }
            }
        },
        colors = CustomListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@Composable
fun MyListCardShimmer(
    modifier: Modifier = Modifier
) {
    CustomListItem(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .shimmerEffect(),
        enabled = false,
        onClick = {},
        contentPadding = PaddingValues(8.dp),
        leadingContent = {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
                    .padding(2.dp)
                    .size(64.dp)
            )
        },
        headlineContent = {
            Text(
                text = "",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}