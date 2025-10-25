package com.personal.tmdb.detail.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.fadingEdges
import com.personal.tmdb.core.domain.util.formatDate
import com.personal.tmdb.core.domain.util.formatVoteAverage
import com.personal.tmdb.core.domain.util.getColorForVoteAverage
import com.personal.tmdb.core.presentation.components.CustomDragHandleWithButton
import com.personal.tmdb.detail.domain.models.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBottomSheet(
    modifier: Modifier = Modifier,
    review: Review,
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()
    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
        SelectionContainer {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    review.rating?.let { rating ->
                        DisableSelection {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .border(
                                        border = BorderStroke(
                                            width = 2.dp,
                                            color = getColorForVoteAverage(rating)
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = formatVoteAverage(rating),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        review.author?.let { author ->
                            Text(
                                text = author,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        review.createdAt?.let { createdAt ->
                            Text(
                                text = formatDate(createdAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .fadingEdges(
                            state = scrollState,
                            topEdgeHeight = 8.dp,
                            bottomEdgeHeight = 8.dp
                        )
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = review.content ?: stringResource(R.string.empty_review),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
                )
            }
        }
    }
}