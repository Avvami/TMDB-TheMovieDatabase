package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

@Composable
fun MinVoteCountFilter(
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
        Text(
            text = stringResource(id = R.string.minimumVoteCount),
            style = MaterialTheme.typography.titleMedium
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = filtersState().minimumVoteCount,
            onValueChange = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetMinVoteCount(it))
            },
            placeholder = { Text(text = FiltersState().minimumVoteCountDefault.toString()) },
            prefix = { Text(modifier = Modifier.padding(end = 8.dp), text = stringResource(id = R.string.from)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedPrefixColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedPrefixColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
        )
    }
}