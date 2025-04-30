package com.personal.tmdb.discover.presentation.discover_filters.components

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

@Composable
fun RatingFilter(
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
                text = stringResource(id = R.string.rating),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            AnimatedVisibility(
                visible = filtersState().ratingApplied,
                enter = fadeIn(tween(100)),
                exit = fadeOut(tween(100))
            ) {
                Text(
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        filtersUiEvent(DiscoverFiltersUiEvent.ClearRatingFilter)
                    },
                    text = stringResource(id = R.string.clear),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = filtersState().fromRating,
            onValueChange = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetFromRating(it))
            },
            placeholder = { Text(text = FiltersState().fromRatingDefault.toString()) },
            prefix = { Text(modifier = Modifier.padding(end = 8.dp), text = stringResource(id = R.string.from)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = filtersState().toRating,
            onValueChange = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetToRating(it))
            },
            placeholder = { Text(text = FiltersState().toRatingDefault.toString()) },
            prefix = { Text(modifier = Modifier.padding(end = 8.dp), text = stringResource(id = R.string.to)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
        )
        Text(
            text = stringResource(id = R.string.minimumVoteCount),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.surfaceVariant
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
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
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