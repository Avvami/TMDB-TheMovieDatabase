package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.personal.tmdb.core.domain.util.compactDecimalFormat
import com.personal.tmdb.core.domain.util.formatVoteAverage
import com.personal.tmdb.core.domain.util.getColorForVoteAverage
import com.personal.tmdb.detail.presentation.detail.DetailState

@Composable
fun Rating(
    modifier: Modifier = Modifier,
    detailState: DetailState
) {
    detailState.details?.let { details ->
        Text(
            modifier = modifier,
            text = buildAnnotatedString {
                details.voteAverage?.let { voteAverage ->
                    withStyle(
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = getColorForVoteAverage(voteAverage)
                        ).toSpanStyle()
                    ) {
                        append(formatVoteAverage(voteAverage))
                    }
                }
                details.voteCount?.takeIf { it != 0 }?.let { voteCount ->
                    withStyle(
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ).toSpanStyle()
                    ) {
                        append(" (${compactDecimalFormat(voteCount.toLong())})")
                    }
                }
                details.originalName.takeIf { it != details.name }?.let { originalName ->
                    withStyle(
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ).toSpanStyle()
                    ) {
                        append("  $originalName")
                    }
                }
            },
            textAlign = TextAlign.Center
        )
    }
}