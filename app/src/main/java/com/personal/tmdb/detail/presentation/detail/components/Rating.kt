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
import com.personal.tmdb.ui.theme.onSurfaceDark
import com.personal.tmdb.ui.theme.surfaceVariantDark

@Composable
fun Rating(
    modifier: Modifier = Modifier,
    detailState: DetailState
) {
    detailState.details?.let { details ->
        details.voteAverage?.let { voteAverage ->
            Text(
                modifier = modifier,
                text = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = getColorForVoteAverage(voteAverage)
                        ).toSpanStyle()
                    ) {
                        append(formatVoteAverage(voteAverage))
                    }
                    details.voteCount?.let { voteCount ->
                        withStyle(
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = surfaceVariantDark
                            ).toSpanStyle()
                        ) {
                            append(" (${compactDecimalFormat(voteCount.toLong())})")
                        }
                    }
                    details.originalName.takeIf { it != details.name }?.let { originalName ->
                        withStyle(
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = onSurfaceDark
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
}