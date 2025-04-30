package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    showModal: Boolean,
    state: DatePickerState = rememberDatePickerState(),
    onDateSelected: (dateMillis: Long?) -> Unit,
    onDismissRequest: () -> Unit,
    dateFormatter: DatePickerFormatter = remember { DatePickerDefaults.dateFormatter() },
    title: (@Composable () -> Unit)? = {
        DatePickerDefaults.DatePickerTitle(
            displayMode = state.displayMode,
            modifier = Modifier.padding(DatePickerTitlePadding)
        )
    }
) {
    if (showModal) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(state.selectedDateMillis)
                        onDismissRequest()
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            DatePicker(
                state = state,
                dateFormatter = dateFormatter,
                title = title,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.surfaceVariant,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    subheadContentColor = MaterialTheme.colorScheme.surfaceVariant,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationContentColor = MaterialTheme.colorScheme.surfaceVariant,
                    yearContentColor = MaterialTheme.colorScheme.onSurface,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    dividerColor = MaterialTheme.colorScheme.surfaceVariant,
                    dateTextFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedLabelColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
        }
    }
}

val DatePickerTitlePadding = PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)