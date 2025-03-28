package com.personal.tmdb.detail.presentation.detail.components

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.core.presentation.components.CustomDragHandle
import com.personal.tmdb.detail.data.models.Rated
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RatingBottomSheet(
    modifier: Modifier = Modifier,
    detailState: () -> DetailState,
    onDismissRequest: () -> Unit,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    val rated = detailState().details?.accountStates?.rated
    val pagerState = rememberPagerState(
        pageCount = { 11 },
        initialPage = when (rated) {
            is Rated.Value -> rated.value; else -> 0
        }
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val indicatorColor by animateColorAsState(
        targetValue = if (rated is Rated.Value && rated.value == pagerState.currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        label = "Indicator animated color"
    )
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismissRequest,
        dragHandle = { CustomDragHandle() },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.rate),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .height(170.dp)
                            .aspectRatio(2 / 3f)
                            .clip(MaterialTheme.shapes.medium),
                        model = C.TMDB_IMAGES_BASE_URL + C.POSTER_W300 + detailState().details?.posterPath,
                        contentDescription = "Poster",
                        placeholder = painterResource(id = R.drawable.placeholder),
                        error = painterResource(id = R.drawable.placeholder),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = detailState().details?.name ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides if (Build.VERSION.SDK_INT > 30) OverscrollConfiguration() else null
                ) {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val contentPadding = (maxWidth - RateButtonComponentSize) / 2
                        Box(
                            modifier = Modifier
                                .size(RateBarHeight)
                                .clip(CircleShape)
                                .background(indicatorColor)
                        )
                        HorizontalPager(
                            modifier = Modifier.height(RateBarHeight),
                            state = pagerState,
                            flingBehavior = PagerDefaults.flingBehavior(
                                state = pagerState,
                                pagerSnapDistance = PagerSnapDistance.atMost(10)
                            ),
                            contentPadding = PaddingValues(horizontal = contentPadding),
                            pageSpacing = 24.dp
                        ) { page ->
                            val textColor by animateColorAsState(
                                targetValue = when {
                                    rated is Rated.Value && rated.value == pagerState.currentPage && rated.value == page -> MaterialTheme.colorScheme.onPrimary
                                    rated is Rated.Value && rated.value == page -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onSurface
                                },
                                label = "Text animated color"
                            )
                            val textSize by animateFloatAsState(
                                targetValue = when {
                                    detailState().rating && pagerState.currentPage == page -> MaterialTheme.typography.displaySmall.fontSize.value
                                    else -> MaterialTheme.typography.displayMedium.fontSize.value
                                },
                                label = "Text animated size"
                            )
                            Box(
                                modifier = Modifier
                                    .size(RateButtonComponentSize)
                                    .clickable(
                                        indication = null,
                                        interactionSource = null
                                    ) {
                                        scope.launch {
                                            pagerState.animateScrollToPage(page)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (page == 0) stringResource(id = R.string.dash) else page.toString(),
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = textSize.sp,
                                    color = if (page == 0) MaterialTheme.colorScheme.surfaceVariant else textColor
                                )
                            }
                        }
                    }
                }
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                ) {
                    Button(
                        onClick = {
                            when (rated) {
                                Rated.NotRated -> {
                                    if (pagerState.currentPage == 0) {
                                        onDismissRequest()
                                    } else {
                                        detailUiEvent(
                                            DetailUiEvent.SetRating(
                                                mediaType = detailState().mediaType,
                                                mediaId = detailState().mediaId,
                                                rating = pagerState.currentPage
                                            )
                                        )
                                    }
                                }
                                is Rated.Value -> {
                                    if (rated.value == pagerState.currentPage) {
                                        context.shareText(C.SHARE_MEDIA.format(detailState().mediaType.name.lowercase(), detailState().mediaId))
                                    } else {
                                        detailUiEvent(
                                            DetailUiEvent.SetRating(
                                                mediaType = detailState().mediaType,
                                                mediaId = detailState().mediaId,
                                                rating = pagerState.currentPage
                                            )
                                        )
                                    }
                                }
                                null -> Unit
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (pagerState.currentPage) {
                                0 -> {
                                    if (rated is Rated.Value) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                }
                                else -> {
                                    if (rated is Rated.Value) {
                                        if (rated.value == pagerState.currentPage) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    }
                                }
                            },
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        when (pagerState.currentPage) {
                            0 -> {
                                if (rated is Rated.Value) {
                                    Text(
                                        text = stringResource(id = R.string.remove_rating),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.dont_rate),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            else -> {
                                if (rated is Rated.Value) {
                                    if (rated.value == pagerState.currentPage) {
                                        Icon(
                                            imageVector = Icons.Rounded.Share,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                                        Text(
                                            text = stringResource(id = R.string.share),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    } else {
                                        Text(
                                            text = stringResource(id = R.string.change_rating),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.rate),
                                        style = MaterialTheme.typography.titleMedium
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

private val RateButtonComponentSize = 56.dp
private val RateBarHeight = 80.dp