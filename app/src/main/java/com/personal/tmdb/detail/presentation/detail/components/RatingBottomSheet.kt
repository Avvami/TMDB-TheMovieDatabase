package com.personal.tmdb.detail.presentation.detail.components

import android.os.Build
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberPlatformOverscrollFactory
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.clickableNoIndication
import com.personal.tmdb.core.domain.util.getColorForVoteAverage
import com.personal.tmdb.core.domain.util.indicatorOffsetForPage
import com.personal.tmdb.core.presentation.LoadState
import com.personal.tmdb.core.presentation.components.CustomDragHandleWithButton
import com.personal.tmdb.detail.data.models.Rated
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.onSurfaceDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun RatingBottomSheet(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    onDismissRequest: () -> Unit,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    val rated = detailState.accountState?.rated
    val ratedValue = when (rated) {
        is Rated.Value -> rated.value
        else -> 0
    }
    val pagerState = rememberPagerState(
        pageCount = { RATING_PAGES },
        initialPage = ratedValue
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.rate),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .height(200.dp)
                        .aspectRatio(2 / 3f)
                        .clip(MaterialTheme.shapes.medium),
                    model = TMDB.posterW300(detailState.details?.posterPath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = detailState.details?.name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            CompositionLocalProvider(
                LocalOverscrollFactory provides if (Build.VERSION.SDK_INT > 30) {
                    rememberPlatformOverscrollFactory()
                } else null
            ) {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val contentPadding = (maxWidth - RateButtonComponentSize) / 2
                    val indicatorOffset = pagerState.indicatorOffsetForPage(ratedValue)
                    val animatedIndicatorColor = lerp(
                        MaterialTheme.colorScheme.surface,
                        if (ratedValue == 0) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            getColorForVoteAverage(ratedValue.toFloat())
                        },
                        indicatorOffset
                    )
                    val indicatorPulseAnim = remember { Animatable(1f) }
                    LaunchedEffect(detailState.ratingLoadState) {
                        if (detailState.ratingLoadState is LoadState.Loading
                            && !indicatorPulseAnim.isRunning) {
                            while (true) {
                                indicatorPulseAnim.animateTo(
                                    targetValue = .6f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                                indicatorPulseAnim.animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        } else {
                            indicatorPulseAnim.stop()
                            indicatorPulseAnim.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = indicatorPulseAnim.value
                                scaleY = indicatorPulseAnim.value
                            }
                            .size(RateBarHeight)
                            .clip(CircleShape)
                            .background(animatedIndicatorColor)
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
                        val pageOffset = pagerState.indicatorOffsetForPage(page)
                        val animatedTextScale = lerp(1f, 1.6f, pageOffset)
                        val animatedTextColor = lerp(
                            if (ratedValue == page) {
                                getColorForVoteAverage(page.toFloat())
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            if (ratedValue == pagerState.currentPage) {
                                onSurfaceDark
                            } else {
                                getColorForVoteAverage(page.toFloat())
                            },
                            pageOffset
                        )
                        Box(
                            modifier = Modifier
                                .size(RateButtonComponentSize)
                                .clickableNoIndication {
                                    scope.launch { pagerState.animateScrollToPage(page) }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.graphicsLayer {
                                    scaleX = animatedTextScale
                                    scaleY = animatedTextScale
                                },
                                text = if (page == 0) {
                                    stringResource(id = R.string.dash)
                                } else {
                                    page.toString()
                                },
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Black,
                                color = if (page == 0) {
                                    MaterialTheme.colorScheme.surfaceVariant
                                } else {
                                    animatedTextColor
                                }
                            )
                        }
                    }
                }
            }
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified
            ) {
                LookaheadScope {
                    val animatedContainerColor by animateColorAsState(
                        targetValue = when {
                            pagerState.currentPage == 0 -> MaterialTheme.colorScheme.onSurface
                            rated is Rated.NotRated -> getColorForVoteAverage(pagerState.currentPage.toFloat())
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                    val animatedContentColor by animateColorAsState(
                        targetValue = when {
                            pagerState.currentPage == 0 -> MaterialTheme.colorScheme.surface
                            rated is Rated.NotRated -> onSurfaceDark
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                    Button(
                        modifier = Modifier.animateBounds(lookaheadScope = this),
                        onClick = {
                            if (detailState.ratingLoadState is LoadState.Loading) return@Button
                            when (rated) {
                                Rated.NotRated -> {
                                    if (pagerState.currentPage == 0) {
                                        onDismissRequest()
                                    } else {
                                        detailUiEvent(DetailUiEvent.SetRating(pagerState.currentPage))
                                    }
                                }
                                is Rated.Value -> {
                                    if (rated.value == pagerState.currentPage) {
                                        detailUiEvent(DetailUiEvent.Share)
                                    } else {
                                        detailUiEvent(DetailUiEvent.SetRating(pagerState.currentPage))
                                    }
                                }
                                null -> onDismissRequest()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = animatedContainerColor,
                            contentColor = animatedContentColor
                        ),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        when (pagerState.currentPage) {
                            0 -> {
                                if (rated is Rated.Value) {
                                    Text(
                                        modifier = Modifier.animateBounds(lookaheadScope = this@LookaheadScope),
                                        text = stringResource(id = R.string.remove_rating),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                } else {
                                    Text(
                                        modifier = Modifier.animateBounds(lookaheadScope = this@LookaheadScope),
                                        text = stringResource(id = R.string.dont_rate),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            else -> {
                                if (rated is Rated.Value) {
                                    if (rated.value == pagerState.currentPage) {
                                        Row(
                                            modifier = Modifier.animateBounds(lookaheadScope = this@LookaheadScope),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.icon_share_fill0_wght400),
                                                contentDescription = null
                                            )
                                            Text(
                                                text = stringResource(id = R.string.share),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    } else {
                                        Text(
                                            modifier = Modifier.animateBounds(lookaheadScope = this@LookaheadScope),
                                            text = stringResource(id = R.string.change_rating),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                } else {
                                    Text(
                                        modifier = Modifier.animateBounds(lookaheadScope = this@LookaheadScope),
                                        text = stringResource(id = R.string.rate),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private const val RATING_PAGES = 11
private val RateButtonComponentSize = 64.dp
private val RateBarHeight = 110.dp