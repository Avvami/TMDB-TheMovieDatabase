package com.personal.tmdb.detail.presentation.reviews

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.detail.presentation.reviews.components.Review
import com.personal.tmdb.detail.presentation.reviews.components.ReviewsScreenShimmer
import com.personal.tmdb.detail.presentation.reviews.components.ReviewsSelected

@Composable
fun ReviewsScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    viewModel: ReviewsViewModel = hiltViewModel()
) {
    val reviewsState by viewModel.reviewsState.collectAsStateWithLifecycle()
    ReviewsScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        reviewsState = { reviewsState },
        reviewsUiEvent = { event ->
            when (event) {
                ReviewsUiEvent.OnNavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.reviewsUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun ReviewsScreen(
    modifier: Modifier = Modifier,
    reviewsState: () -> ReviewsState,
    reviewsUiEvent: (ReviewsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = !reviewsState().showSelectedReview,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = stringResource(id = R.string.reviews),
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (reviewsState().showSelectedReview && reviewsState().reviews?.results?.size != 1) {
                                reviewsUiEvent(ReviewsUiEvent.SetSelectedReview(reviewsState().selectedReviewIndex, false))
                            } else {
                                reviewsUiEvent(ReviewsUiEvent.OnNavigateBack)
                            }
                        }
                    )  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        val lazyGridState = rememberLazyStaggeredGridState()
        LaunchedEffect(key1 = true) {
            lazyGridState.scrollToItem(reviewsState().selectedReviewIndex)
        }
        BackHandler {
            if (reviewsState().showSelectedReview && reviewsState().reviews?.results?.size != 1) {
                reviewsUiEvent(ReviewsUiEvent.SetSelectedReview(reviewsState().selectedReviewIndex, false))
            } else {
                reviewsUiEvent(ReviewsUiEvent.OnNavigateBack)
            }
        }
        SharedTransitionLayout(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            if (reviewsState().loading && reviewsState().reviews == null) {
                ReviewsScreenShimmer(
                    showReview = reviewsState().showSelectedReview,
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                )
            } else {
                reviewsState().reviews?.results?.let { reviews ->
                    AnimatedContent(
                        targetState = reviewsState().showSelectedReview,
                        label = "Selected review transition"
                    ) { targetState ->
                        if (targetState) {
                            ReviewsSelected(
                                modifier = Modifier
                                    .sharedBounds(
                                        animatedVisibilityScope = this,
                                        sharedContentState = rememberSharedContentState(
                                            key = C.REVIEW.format(
                                                reviewsState().selectedReviewIndex
                                            )
                                        )
                                    )
                                    .verticalScroll(rememberScrollState())
                                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                                review = reviews[reviewsState().selectedReviewIndex]
                            )
                        } else {
                            LazyVerticalStaggeredGrid(
                                state = lazyGridState,
                                columns = StaggeredGridCells.Adaptive(300.dp),
                                contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                                verticalItemSpacing = 16.dp,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(
                                    items = reviews,
                                    key = { _, item -> item.id }
                                ) { index, review ->
                                    Review(
                                        modifier = Modifier
                                            .sharedBounds(
                                                animatedVisibilityScope = this@AnimatedContent,
                                                sharedContentState = rememberSharedContentState(
                                                    key = C.REVIEW.format(
                                                        index
                                                    )
                                                )
                                            )
                                            .fillMaxWidth()
                                            .clip(MaterialTheme.shapes.medium)
                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                            .clickable {
                                                reviewsUiEvent(
                                                    ReviewsUiEvent.SetSelectedReview(index, true)
                                                )
                                            }
                                            .padding(16.dp),
                                        review = review
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