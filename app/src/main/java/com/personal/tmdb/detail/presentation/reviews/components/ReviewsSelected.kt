package com.personal.tmdb.detail.presentation.reviews.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.presentation.components.HtmlSelectableTextContainer
import com.personal.tmdb.core.domain.util.formatDate
import com.personal.tmdb.detail.domain.models.Review

@Composable
fun ReviewsSelected(
    modifier: Modifier = Modifier,
    review: Review
) {
    with(review) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                ReviewAnnotatedText(
                    modifier = Modifier.weight(1f),
                    review = review
                )
                authorDetails?.rating?.let { rating ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                            contentDescription = "Thumbs",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = rating.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            content?.let { content ->
                HtmlSelectableTextContainer(
                    text = content
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewAnnotatedText(
    modifier: Modifier = Modifier,
    review: Review
) {
    val accentStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary).toSpanStyle()
    val textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.surfaceVariant).toSpanStyle()
    val annotatedString = remember {
        buildAnnotatedString {
            withStyle(textStyle) {
                append("Written by")
            }
            withStyle(accentStyle) {
                append(" ${review.author ?: "Unknown"}")
            }
            review.createdAt?.let { createdAt ->
                withStyle(textStyle) {
                    append(" on ${formatDate(createdAt)}")
                }
            }
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString
    )
}