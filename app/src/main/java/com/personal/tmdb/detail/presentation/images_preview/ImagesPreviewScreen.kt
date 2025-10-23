package com.personal.tmdb.detail.presentation.images_preview

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberPlatformOverscrollFactory
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.personal.tmdb.MainActivity
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.applyStatusBarsTheme
import com.personal.tmdb.core.domain.util.findActivity
import com.personal.tmdb.core.domain.util.hideSystemBars
import com.personal.tmdb.core.domain.util.shareText
import com.personal.tmdb.core.domain.util.showSystemBars
import com.personal.tmdb.ui.theme.onSurfaceDark
import net.engawapg.lib.zoomable.ScrollGesturePropagation
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ImagesPreviewScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: ImagesPreviewViewModel = hiltViewModel()
) {
    val view = LocalView.current
    val context = LocalContext.current
    val activity = context.findActivity() as MainActivity
    val imagesPreviewState by viewModel.imagesPreviewState.collectAsStateWithLifecycle()
    DisposableEffect(Unit) {
        applyStatusBarsTheme(
            view = view,
            context = context,
            applyLightStatusBars = true
        )
        onDispose {
            activity.showSystemBars()
        }
    }
    ImagesPreviewScreen(
        imagesPreviewState = { imagesPreviewState },
        imagesPreviewUiEvent = { event ->
            when (event) {
                ImagesPreviewUiEvent.OnNavigateBack -> onNavigateBack()
                is ImagesPreviewUiEvent.SetUiHidden -> {
                    if (event.state) {
                        activity.hideSystemBars()
                    } else {
                        activity.showSystemBars()
                    }
                }
                is ImagesPreviewUiEvent.ShareImage -> {
                    context.shareText(C.SHARE_IMAGE.format(event.filePath))
                }
            }
            viewModel.imagesPreviewUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImagesPreviewScreen(
    modifier: Modifier = Modifier,
    imagesPreviewState: () -> ImagesPreviewState,
    imagesPreviewUiEvent: (ImagesPreviewUiEvent) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = imagesPreviewState().selectedIndex,
        pageCount = { imagesPreviewState().filePaths.size }
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim)
    ) {
        CompositionLocalProvider(
            LocalOverscrollFactory provides if (Build.VERSION.SDK_INT > 30) {
                rememberPlatformOverscrollFactory()
            } else null
        ) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                state = pagerState,
                beyondViewportPageCount = 2,
                pageSize = PageSize.Fill
            ) { page ->
                val zoomState = rememberZoomState(maxScale = 4f)
                AsyncImage(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxSize()
                        .graphicsLayer {
                            val endOffset = (
                                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                    ).coerceAtMost(0f)
                            alpha = 1f + endOffset
                            val scale = 1f + (endOffset * .2f)
                            scaleX = scale
                            scaleY = scale
                        }
                        .zoomable(
                            zoomState = zoomState,
                            enableOneFingerZoom = false,
                            scrollGesturePropagation = ScrollGesturePropagation.ContentEdge,
                            onTap = {
                                imagesPreviewUiEvent(
                                    ImagesPreviewUiEvent.SetUiHidden(!imagesPreviewState().uiHidden)
                                )
                            }
                        ),
                    model = TMDB.imageOriginal(imagesPreviewState().filePaths[page]),
                    onSuccess = { state ->
                        zoomState.setContentSize(state.painter.intrinsicSize)
                    },
                    contentDescription = null
                )
                val isVisible = page == pagerState.settledPage
                LaunchedEffect(key1 = isVisible) {
                    if (!isVisible) {
                        zoomState.reset()
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = !imagesPreviewState().uiHidden,
            enter = slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessLow))
                    + fadeIn(animationSpec = tween(delayMillis = 50)),
            exit = slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessLow))
                    + fadeOut(animationSpec = tween(durationMillis = 250))
        ) {
            TopAppBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.scrim.copy(.5f))
                    .statusBarsPadding(),
                title = {
                    Text(
                        text = imagesPreviewState().name,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow_back_fill0_wght400),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            imagesPreviewUiEvent(
                                ImagesPreviewUiEvent.ShareImage(
                                    filePath = imagesPreviewState().filePaths[pagerState.currentPage]
                                )
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_share_fill0_wght400),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = onSurfaceDark,
                    titleContentColor = onSurfaceDark,
                    actionIconContentColor = onSurfaceDark
                )
            )
        }
    }
}