package com.personal.tmdb.detail.presentation.person.components

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.detail.domain.models.PersonInfo
import com.personal.tmdb.detail.domain.util.ImageType
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonPhotoCarousel(
    modifier: Modifier = Modifier,
    onNavigateTo: (route: Route) -> Unit,
    personInfo: () -> PersonInfo
) {
    val pageCount = if ((personInfo().images?.profiles?.size ?: 0) > 1) 250 else 1
    val horizontalPagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = pageCount / 2
    )
    BoxWithConstraints(
        modifier = modifier
    ) {
        val contentPadding = (maxWidth - 150.dp) / 2
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides if (Build.VERSION.SDK_INT > 30) OverscrollConfiguration() else null
        ) {
            HorizontalPager(
                state = horizontalPagerState,
                contentPadding = PaddingValues(horizontal = contentPadding),
                pageSpacing = (maxWidth / 150) - 8.dp,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = horizontalPagerState,
                    pagerSnapDistance = PagerSnapDistance.atMost(3)
                )
            ) { page ->
                if (personInfo().images?.profiles.isNullOrEmpty()) {
                    AsyncImage(
                        modifier = Modifier
                            .height(230.dp)
                            .aspectRatio(2 / 3f)
                            .clip(MaterialTheme.shapes.large),
                        model = C.TMDB_IMAGES_BASE_URL + C.POSTER_W300,
                        placeholder = painterResource(id = R.drawable.placeholder),
                        error = painterResource(id = R.drawable.placeholder),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop
                    )
                } else {
                    personInfo().images?.profiles?.let { profiles ->
                        AsyncImage(
                            modifier = Modifier
                                .height(230.dp)
                                .aspectRatio(2 / 3f)
                                .graphicsLayer {
                                    val pageOffset = (
                                            (horizontalPagerState.currentPage - page) + horizontalPagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue
                                    lerp(
                                        start = .85f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                                    ).also { scale ->
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                }
                                .clip(MaterialTheme.shapes.large)
                                .clickable {
                                    onNavigateTo(
                                        Route.Images(
                                            imageType = ImageType.PROFILES.name.lowercase(),
                                            imagesPath = C.MEDIA_IMAGES.format(MediaType.PERSON.name.lowercase(), personInfo().id),
                                            selectedImageIndex = page % profiles.size
                                        )
                                    )
                                },
                            model = C.TMDB_IMAGES_BASE_URL + C.POSTER_W300 + profiles[page % profiles.size]?.filePath,
                            placeholder = painterResource(id = R.drawable.placeholder),
                            error = painterResource(id = R.drawable.placeholder),
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}