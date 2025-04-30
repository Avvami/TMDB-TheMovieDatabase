package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults
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
import com.personal.tmdb.core.domain.util.capitalizeFirstLetter
import com.personal.tmdb.core.domain.util.fadingEdges
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.core.presentation.components.HorizontalSegmentedButton
import com.personal.tmdb.core.presentation.components.SegmentedButtonsRow
import com.personal.tmdb.discover.presentation.discover_filters.ContentOriginType
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

@Composable
fun ContentOriginFilter(
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
                text = stringResource(id = R.string.content_origin),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            AnimatedVisibility(
                visible = filtersState().contentOriginApplied,
                enter = fadeIn(tween(100)),
                exit = fadeOut(tween(100))
            ) {
                Text(
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        filtersUiEvent(DiscoverFiltersUiEvent.ClearContentOriginFilter)
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
                    filtersUiEvent(DiscoverFiltersUiEvent.SetContentOriginType(ContentOriginType.COUNTRY))
                },
                selected = filtersState().contentOriginType == ContentOriginType.COUNTRY,
                shape = MaterialTheme.shapes.small,
                label = {
                    Text(text = stringResource(id = R.string.country))
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
                    filtersUiEvent(DiscoverFiltersUiEvent.SetContentOriginType(ContentOriginType.LANGUAGE))
                },
                selected = filtersState().contentOriginType == ContentOriginType.LANGUAGE,
                shape = MaterialTheme.shapes.small,
                label = {
                    Text(text = stringResource(id = R.string.language))
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.surface,
                    activeContentColor = MaterialTheme.colorScheme.primary,
                    inactiveContainerColor = Color.Transparent,
                    inactiveContentColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = filtersState().searchQuery,
            onValueChange = {
                filtersUiEvent(DiscoverFiltersUiEvent.SetSearchQuery(it))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            },
            placeholder = {
                Text(text = stringResource(id = R.string.search))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
        )
        AnimatedContent(
            targetState = filtersState().contentOriginType,
            label = "ContentOriginListAnimation"
        ) { contentOrigin ->
            when (contentOrigin) {
                ContentOriginType.COUNTRY -> {
                    filtersState().countries?.let { countries ->
                        val lazyListState = rememberLazyListState()
                        LazyColumn(
                            modifier = Modifier.fadingEdges(lazyListState),
                            state = lazyListState
                        ) {
                            items(
                                items = countries,
                                key = { it.code },
                                contentType = { "Origin" }
                            ) { country ->
                                CustomListItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem(),
                                    onClick = {
                                        filtersUiEvent(DiscoverFiltersUiEvent.SelectOrigin(country))
                                    },
                                    headlineContent = {
                                        Text(text = capitalizeFirstLetter(country.locale.displayCountry))
                                    },
                                    leadingContent = {
                                        AnimatedContent(
                                            targetState = filtersState().selectedOrigin,
                                            label = "SelectedOriginCheckAnimation"
                                        ) { selectedOrigin ->
                                            if (selectedOrigin == country) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Check,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    },
                                    trailingContent = {
                                        Text(text = capitalizeFirstLetter(country.code))
                                    },
                                    contentPadding = PaddingValues(vertical = 12.dp),
                                    shape = MaterialTheme.shapes.medium
                                )
                            }
                        }
                    }
                }
                ContentOriginType.LANGUAGE -> {
                    filtersState().languages?.let { languages ->
                        val lazyListState = rememberLazyListState()
                        LazyColumn(
                            modifier = Modifier.fadingEdges(lazyListState),
                            state = lazyListState
                        ) {
                            items(
                                items = languages,
                                key = { it.code },
                                contentType = { "Origin" }
                            ) { language ->
                                CustomListItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem(),
                                    onClick = {
                                        filtersUiEvent(DiscoverFiltersUiEvent.SelectOrigin(language))
                                    },
                                    headlineContent = {
                                        Text(
                                            text = capitalizeFirstLetter(language.locale.displayLanguage)
                                        )
                                    },
                                    leadingContent = {
                                        AnimatedContent(
                                            targetState = filtersState().selectedOrigin,
                                            label = "SelectedOriginCheckAnimation"
                                        ) { selectedOrigin ->
                                            if (selectedOrigin == language) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Check,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    },
                                    trailingContent = {
                                        Text(text = capitalizeFirstLetter(language.code))
                                    },
                                    contentPadding = PaddingValues(vertical = 12.dp),
                                    shape = MaterialTheme.shapes.medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}