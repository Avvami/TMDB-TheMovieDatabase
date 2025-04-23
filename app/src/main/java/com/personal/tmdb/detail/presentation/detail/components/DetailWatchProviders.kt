package com.personal.tmdb.detail.presentation.detail.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowUp
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImage
import com.personal.tmdb.MainActivity
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.findActivity
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.detail.data.models.Provider
import com.personal.tmdb.detail.presentation.detail.DetailState
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.ui.theme.justWatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailWatchProviders(
    modifier: Modifier = Modifier,
    detailState: () -> DetailState,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val activity = LocalContext.current.findActivity() as MainActivity
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.powered_by),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Image(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable { activity.openCustomChromeTab(C.JUSTWATCH_URL) }
                    .background(justWatch)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                painter = painterResource(id = R.drawable.icon_just_watch),
                contentDescription = "JustWatch"
            )
        }
        Column {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .clip(MaterialTheme.shapes.large),
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
                            Text(text = detailState().watchCountry)
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
                                            imageVector = Icons.Rounded.Clear,
                                            contentDescription = "Clear"
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
                                            imageVector = Icons.Rounded.KeyboardArrowUp,
                                            contentDescription = "Close"
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
                shape = MaterialTheme.shapes.large,
                content = {
                    LazyColumn {
                        detailState().watchCountries?.let { watchCountries ->
                            items(
                                items = watchCountries.entries.toList(),
                                key = { it.key }
                            ) { (countryCode, countryName) ->
                                CustomListItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem(),
                                    onClick = {
                                        detailUiEvent(DetailUiEvent.SetSelectedCountry(countryName))
                                        searchQuery = ""
                                        searchActive = false
                                    },
                                    headlineContent = {
                                        Text(text = countryName)
                                    },
                                    leadingContent = {
                                        AsyncImage(
                                            modifier = Modifier
                                                .height(16.dp)
                                                .aspectRatio(4 / 3f)
                                                .clip(MaterialTheme.shapes.extraSmall),
                                            model = C.FLAG_ICONS_URL.format(countryCode.lowercase()),
                                            placeholder = painterResource(id = R.drawable.placeholder),
                                            error = painterResource(id = R.drawable.placeholder),
                                            contentDescription = "Flag",
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                    dividerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                detailState().details?.watchProviders?.get(detailState().watchCountry)?.let { available ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .clip(RoundedCornerShape(4.dp, 8.dp, 8.dp, 4.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f)),
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
                            onClick = { activity.openCustomChromeTab(available.link) },
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
                    available.flatrate?.let { stream ->
                        Providers(
                            modifier = Modifier
                                .fillMaxWidth(),
                            categoryRes = R.string.stream,
                            providers = stream
                        )
                    }
                    available.free?.let { free ->
                        Providers(
                            modifier = Modifier
                                .fillMaxWidth(),
                            categoryRes = R.string.free,
                            providers = free
                        )
                    }
                    available.buy?.let { buy ->
                        Providers(
                            modifier = Modifier
                                .fillMaxWidth(),
                            categoryRes = R.string.buy,
                            providers = buy
                        )
                    }
                    available.rent?.let { rent ->
                        Providers(
                            modifier = Modifier
                                .fillMaxWidth(),
                            categoryRes = R.string.rent,
                            providers = rent
                        )
                    }
                    available.ads?.let { ads ->
                        Providers(
                            modifier = Modifier
                                .fillMaxWidth(),
                            categoryRes = R.string.ads,
                            providers = ads
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Providers(
    modifier: Modifier = Modifier,
    @StringRes categoryRes: Int,
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
            text = stringResource(id = categoryRes),
            style = MaterialTheme.typography.titleMedium
        )
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            providers.fastForEach { source ->
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small),
                    model = C.TMDB_IMAGES_BASE_URL + C.LOGO_W92 + source.logoPath,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    contentDescription = source.providerName ?: "Logo",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}