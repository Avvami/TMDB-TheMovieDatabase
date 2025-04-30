package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.formatDate
import com.personal.tmdb.core.presentation.components.HorizontalSegmentedButton
import com.personal.tmdb.core.presentation.components.SegmentedButtonsRow
import com.personal.tmdb.discover.presentation.discover_filters.AirDateType
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirDatesFilter(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.air_dates),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            AnimatedVisibility(
                visible = filtersState().airDateApplied,
                enter = fadeIn(tween(100)),
                exit = fadeOut(tween(100))
            ) {
                Text(
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        filtersUiEvent(DiscoverFiltersUiEvent.ClearAirDateFilter)
                    },
                    text = stringResource(id = R.string.clear),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        SegmentedButtonsRow(
            shape = MaterialTheme.shapes.medium
        ) {
            HorizontalSegmentedButton(
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetAirDateType(AirDateType.RANGE))
                },
                selected = filtersState().airDateType == AirDateType.RANGE,
                shape = MaterialTheme.shapes.small,
                label = {
                    Text(text = stringResource(id = R.string.range))
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.surface,
                    activeContentColor = MaterialTheme.colorScheme.primary,
                    inactiveContainerColor = Color.Transparent,
                    inactiveContentColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            HorizontalSegmentedButton(
                onClick = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetAirDateType(AirDateType.YEAR))
                },
                selected = filtersState().airDateType == AirDateType.YEAR,
                shape = MaterialTheme.shapes.small,
                label = {
                    Text(text = stringResource(id = R.string.year))
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.surface,
                    activeContentColor = MaterialTheme.colorScheme.primary,
                    inactiveContainerColor = Color.Transparent,
                    inactiveContentColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
        AnimatedContent(
            targetState = filtersState().airDateType,
            label = "AirDateContentAnimation"
        ) { airDateType ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (airDateType) {
                    AirDateType.RANGE -> {
                        var showFromDatePicker by rememberSaveable { mutableStateOf(false) }
                        DatePickerModal(
                            showModal = showFromDatePicker,
                            onDateSelected = { dateMillis ->
                                filtersUiEvent(DiscoverFiltersUiEvent.SetFromAirDate(dateMillis))
                            },
                            onDismissRequest = { showFromDatePicker = false }
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { showFromDatePicker = true },
                            enabled = false,
                            value = filtersState().fromAirDate?.let { formatDate(it) } ?: "",
                            onValueChange = {},
                            prefix = { Text(modifier = Modifier.padding(end = 8.dp), text = stringResource(id = R.string.from)) },
                            colors = TextFieldDefaults.colors(
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledIndicatorColor = Color.Transparent,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledPrefixColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium
                        )
                        var showToDatePicker by rememberSaveable { mutableStateOf(false) }
                        DatePickerModal(
                            showModal = showToDatePicker,
                            state = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis()),
                            onDateSelected = { dateMillis ->
                                filtersUiEvent(DiscoverFiltersUiEvent.SetToAirDate(dateMillis))
                            },
                            onDismissRequest = { showToDatePicker = false }
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { showToDatePicker = true },
                            enabled = false,
                            value = filtersState().toAirDate?.let { formatDate(it) } ?: "",
                            onValueChange = {},
                            prefix = { Text(modifier = Modifier.padding(end = 8.dp), text = stringResource(id = R.string.to)) },
                            colors = TextFieldDefaults.colors(
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledIndicatorColor = Color.Transparent,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledPrefixColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                    AirDateType.YEAR -> {
                        var showYearPicker by rememberSaveable { mutableStateOf(false) }
                        DatePickerModal(
                            showModal = showYearPicker,
                            state = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis()),
                            onDateSelected = { dateMillis ->
                                filtersUiEvent(DiscoverFiltersUiEvent.SetYear(dateMillis))
                            },
                            onDismissRequest = { showYearPicker = false },
                            dateFormatter = remember {
                                DatePickerDefaults.dateFormatter(selectedDateSkeleton = "yyyy")
                            },
                            title = {
                                Text(
                                    modifier = Modifier.padding(DatePickerTitlePadding),
                                    text = stringResource(id = R.string.select_year)
                                )
                            }
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { showYearPicker = true },
                            enabled = false,
                            value = filtersState().yearAirDate,
                            onValueChange = {},
                            colors = TextFieldDefaults.colors(
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledIndicatorColor = Color.Transparent,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                }
            }
        }
    }
}