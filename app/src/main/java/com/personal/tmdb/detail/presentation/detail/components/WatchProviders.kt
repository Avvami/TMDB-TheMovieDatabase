package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.detail.domain.models.Provider
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.justWatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchProviders(
    modifier: Modifier = Modifier,
    detailState: DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                .clip(MaterialTheme.shapes.medium),
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        detailUiEvent(DetailUiEvent.FilterWatchCountries(it))
                    },
                    onSearch = {
                        keyboardController?.hide()
                    },
                    expanded = searchActive,
                    onExpandedChange = { searchActive = !searchActive },
                    enabled = true,
                    placeholder = {
                        detailState.selectedCountry?.let { (_, countryName) ->
                            Text(text = countryName)
                        }
                    },
                    trailingIcon = {
                        Row {
                            AnimatedVisibility(
                                visible = searchQuery.isNotEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(
                                    onClick = {
                                        searchQuery = ""
                                        detailUiEvent(DetailUiEvent.FilterWatchCountries(""))
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.icon_close_fill0_wght400),
                                        contentDescription = null
                                    )
                                }
                            }
                            AnimatedVisibility(
                                visible = searchActive,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(onClick = { searchActive = false }) {
                                    Icon(
                                        modifier = Modifier.rotate(180f),
                                        painter = painterResource(R.drawable.icon_keyboard_arrow_down_fill0_wght400),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    },
                    colors = SearchBarDefaults.inputFieldColors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            expanded = searchActive,
            onExpandedChange = { searchActive = !searchActive },
            shape = MaterialTheme.shapes.medium,
            content = {
                LazyColumn {
                    detailState.watchCountries?.let { watchCountries ->
                        items(
                            items = watchCountries.entries.toList(),
                            key = { it.key }
                        ) { country ->
                            CustomListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),
                                onClick = {
                                    detailUiEvent(DetailUiEvent.SetSelectedCountry(country))
                                    searchQuery = ""
                                    searchActive = false
                                },
                                headlineContent = {
                                    Text(text = country.value)
                                },
                                leadingContent = {
                                    AsyncImage(
                                        modifier = Modifier
                                            .height(16.dp)
                                            .aspectRatio(4 / 3f)
                                            .clip(MaterialTheme.shapes.extraSmall),
                                        model = C.FLAG_ICONS_URL.format(country.key.lowercase()),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            )
                        }
                    }
                }
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                dividerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
        )
        AnimatedVisibility(
            visible = !searchActive,
            enter = slideInVertically(initialOffsetY = { it / 20 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 20 })
                    + fadeOut(animationSpec = tween(durationMillis = 90))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    detailState.details?.watchProviders?.get(detailState.selectedCountry?.key)?.let { available ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VerticalDivider(
                                thickness = 4.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                text = stringResource(id = R.string.watch_link_not_provided),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            IconButton(
                                onClick = { detailUiEvent(DetailUiEvent.OpenUrl(available.link)) },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_open_in_new_fill0_wght400),
                                    contentDescription = null
                                )
                            }
                        }
                        available.streaming?.let { stream ->
                            AvailableToWatch(
                                available = UiText.StringResource(R.string.stream),
                                providers = stream
                            )
                        }
                        available.free?.let { free ->
                            AvailableToWatch(
                                available = UiText.StringResource(R.string.free),
                                providers = free
                            )
                        }
                        available.buy?.let { buy ->
                            AvailableToWatch(
                                available = UiText.StringResource(R.string.buy),
                                providers = buy
                            )
                        }
                        available.rent?.let { rent ->
                            AvailableToWatch(
                                available = UiText.StringResource(R.string.rent),
                                providers = rent
                            )
                        }
                        available.ads?.let { ads ->
                            AvailableToWatch(
                                available = UiText.StringResource(R.string.ads),
                                providers = ads
                            )
                        }
                    }
                }
                Image(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { detailUiEvent(DetailUiEvent.OpenUrl(C.JUSTWATCH_URL)) }
                        .background(justWatch)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    painter = painterResource(id = R.drawable.icon_just_watch),
                    contentDescription = "JustWatch"
                )
            }
        }
    }
}

@Composable
fun AvailableToWatch(
    modifier: Modifier = Modifier,
    available: UiText,
    providers: List<Provider>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = available.asString(),
            style = MaterialTheme.typography.titleMedium
        )
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            providers.fastForEach { source ->
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small),
                    model = TMDB.logoW92(source.logoPath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}