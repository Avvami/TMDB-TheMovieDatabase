package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.clickableNoIndication
import com.personal.tmdb.core.domain.util.formatDate
import com.personal.tmdb.core.domain.util.formatVoteAverage
import com.personal.tmdb.core.domain.util.formatVoteCount
import com.personal.tmdb.core.domain.util.getColorForVoteAverage
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.components.MediaCarousel
import com.personal.tmdb.detail.data.models.Rated
import com.personal.tmdb.detail.domain.models.Review
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.onSurfaceDark
import java.time.LocalDate

@Composable
fun RatingReviews(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    userState: () -> UserState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickableNoIndication {
                    detailUiEvent(
                        DetailUiEvent.OnNavigateTo(
                            Route.Reviews(
                                mediaType = detailState.mediaType.name.lowercase(),
                                mediaId = detailState.mediaId
                            )
                        )
                    )
                }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.rating_reviews),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(R.drawable.icon_keyboard_arrow_right_fill0_wght400),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(.35f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                detailState.details?.voteAverage?.let { voteAverage ->
                    Text(
                        text = formatVoteAverage(voteAverage),
                        style = MaterialTheme.typography.displaySmall,
                        color = getColorForVoteAverage(voteAverage),
                        fontWeight = FontWeight.Medium
                    )
                }
                detailState.details?.voteCount?.let { voteCount ->
                    Text(
                        text = formatVoteCount(voteCount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
            if (!userState().user?.sessionId.isNullOrEmpty() &&
                detailState.details?.releaseDate?.isBefore(LocalDate.now()) == true) {
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                ) {
                    Button(
                        modifier = Modifier.weight(.65f),
                        onClick = { detailUiEvent(DetailUiEvent.ShowRatingSheet(true)) },
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        when (val rated = detailState.accountState?.rated) {
                            is Rated.Value -> {
                                Text(
                                    text = stringResource(R.string.change),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(getColorForVoteAverage(rated.value.toFloat()))
                                        .padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    when {
                                        rated.value < 5 -> {
                                            Icon(
                                                modifier = Modifier.size(16.dp),
                                                painter = painterResource(id = R.drawable.icon_thumb_down_fill1_wght400),
                                                contentDescription = null,
                                                tint = onSurfaceDark
                                            )
                                        }
                                        rated.value < 7 -> {
                                            Icon(
                                                modifier = Modifier.size(16.dp),
                                                painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                                                contentDescription = null,
                                                tint = onSurfaceDark
                                            )
                                        }
                                        else -> {
                                            Icon(
                                                modifier = Modifier.size(16.dp),
                                                painter = painterResource(id = R.drawable.icon_thumb_up_fill1_wght400),
                                                contentDescription = null,
                                                tint = onSurfaceDark
                                            )
                                        }
                                    }
                                    Text(
                                        text = rated.value.toString(),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = onSurfaceDark
                                    )
                                }
                            }
                            else -> {
                                Text(
                                    text = stringResource(R.string.rate),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
        detailState.details?.reviews?.take(5)?.let { reviews ->
            if (reviews.size == 1) {
                val review = reviews.first()
                Review(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    review = review,
                    onClick = { detailUiEvent(DetailUiEvent.OpenReview(review)) }
                )
            } else {
                MediaCarousel {
                    items(
                        items = reviews,
                        key = { it.id }
                    ) { review ->
                        Review(
                            modifier = Modifier.width(330.dp),
                            review = review,
                            onClick = { detailUiEvent(DetailUiEvent.OpenReview(review)) }
                        )
                    }
                    if (detailState.details.reviews.size > 5) {
                        item {
                            Column(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .clickableNoIndication {
                                        detailUiEvent(
                                            DetailUiEvent.OnNavigateTo(
                                                Route.Reviews(
                                                    mediaType = detailState.mediaType.name.lowercase(),
                                                    mediaId = detailState.mediaId
                                                )
                                            )
                                        )
                                    }
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.rotate(180f),
                                        painter = painterResource(R.drawable.icon_arrow_left_alt_fill0_wght400),
                                        contentDescription = null
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.show_more),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Review(
    modifier: Modifier = Modifier,
    review: Review,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        review.rating?.let { rating ->
            VerticalDivider(
                thickness = 4.dp,
                color = getColorForVoteAverage(rating)
            )
        }
        Column(
            modifier = Modifier.padding(
                start = if (review.rating == null) 16.dp else 0.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buildList<@Composable FlowRowScope.() -> Unit> {
                review.author?.let { author ->
                    add {
                        Text(
                            text = author,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                review.createdAt?.let { createdAt ->
                    add {
                        Text(
                            text = formatDate(createdAt),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }.takeIf { it.isNotEmpty() }?.let { components ->
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    components.forEachIndexed { index, component ->
                        component()
                        if (index != components.lastIndex) {
                            Icon(
                                modifier = Modifier.size(6.dp),
                                painter = painterResource(id = R.drawable.icon_fiber_manual_record_fill1_wght400),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
            Text(
                text = review.content ?: stringResource(R.string.empty_review),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                minLines = 4,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}