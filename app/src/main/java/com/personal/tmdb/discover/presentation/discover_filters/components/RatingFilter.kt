package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingFilter(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.rating),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(
                    id = R.string.rating_from,
                    filtersState().startRating.roundToInt(),
                    filtersState().endRating.roundToInt()
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        val animatedStartValue by animateFloatAsState(
            targetValue = filtersState().startRating,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
        val animatedEndValue by animateFloatAsState(
            targetValue = filtersState().endRating,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
        RangeSlider(
            modifier = Modifier.fillMaxWidth(),
            value = animatedStartValue..animatedEndValue,
            valueRange = 0f..10f,
            onValueChange = { range ->
                filtersUiEvent(
                    DiscoverFiltersUiEvent.SetRating(
                        startRating = range.start.roundToInt().toFloat(),
                        endRating = range.endInclusive.roundToInt().toFloat()
                    )
                )
            },
            track = { state ->
                SliderDefaults.Track(
                    rangeSliderState = state,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 4.dp,
                    colors = SliderDefaults.colors(
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)
                    )
                )
            }
        )
    }
}