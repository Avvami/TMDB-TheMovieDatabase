package com.personal.tmdb.discover.presentation.discover_filters.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.capitalizeFirstLetter
import com.personal.tmdb.core.domain.util.fadingEdges
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.discover.presentation.discover_filters.DiscoverFiltersUiEvent
import com.personal.tmdb.discover.presentation.discover_filters.FiltersState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CountryFilter(
    modifier: Modifier = Modifier,
    filtersState: () -> FiltersState,
    filtersUiEvent: (DiscoverFiltersUiEvent) -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    var origin by remember {
        mutableStateOf(filtersState().selectedCountry)
    }
    with(sharedTransitionScope) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "Country"),
                        animatedVisibilityScope = animatedContentScope
                    ),
                value = filtersState().searchQuery,
                onValueChange = {
                    filtersUiEvent(DiscoverFiltersUiEvent.SetSearchQuery(it))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.icon_search_fill0_wght400),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = filtersState().searchQuery.isNotEmpty(),
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        IconButton(
                            onClick = {
                                filtersUiEvent(DiscoverFiltersUiEvent.SetSearchQuery(""))
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_close_fill0_wght400),
                                contentDescription = null
                            )
                        }
                    }
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
                    focusedTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
            )
            filtersState().countries?.let { countries ->
                val lazyListState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fadingEdges(lazyListState),
                    state = lazyListState
                ) {
                    items(
                        items = countries,
                        key = { it.code },
                        contentType = { "Country" }
                    ) { country ->
                        CustomListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            onClick = {
                                origin = if (origin == country) {
                                    null
                                } else {
                                    country
                                }
                            },
                            headlineContent = {
                                Text(text = capitalizeFirstLetter(country.locale.displayCountry))
                            },
                            leadingContent = {
                                AnimatedContent(
                                    targetState = origin
                                ) { selectedCountry ->
                                    if (selectedCountry == country) {
                                        Icon(
                                            modifier = Modifier.padding(end = 12.dp),
                                            painter = painterResource(R.drawable.icon_check_fill0_wght400),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            },
                            trailingContent = {
                                Text(text = capitalizeFirstLetter(country.code))
                            },
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 12.dp
                            ),
                            horizontalArrangement = Arrangement.Start,
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        filtersUiEvent(DiscoverFiltersUiEvent.ApplyCountry(origin))
                    },
                    shape = CircleShape,
                    contentPadding = PaddingValues(
                        horizontal = 32.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        text = stringResource(R.string.apply),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}