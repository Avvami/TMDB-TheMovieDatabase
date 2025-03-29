package com.personal.tmdb.settings.presentation.languages

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.capitalizeFirstLetter
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.components.CustomListItem

@Composable
fun LanguagesScreenRoot(
    bottomPadding: Dp,
    onNavigateBack: () -> Unit,
    viewModel: LanguagesViewModel = hiltViewModel()
) {
    val languagesState by viewModel.languagesState.collectAsStateWithLifecycle()
    LanguagesScreen(
        modifier = Modifier.padding(bottom = bottomPadding),
        languagesState = { languagesState },
        languagesUiEvent = { event ->
            when (event) {
                LanguagesUiEvent.OnNavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.languagesUiEvent(event)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagesScreen(
    modifier: Modifier = Modifier,
    languagesState: () -> LanguagesState,
    languagesUiEvent: (LanguagesUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.language),
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { languagesUiEvent(LanguagesUiEvent.OnNavigateBack) }
                    )  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .clip(RoundedCornerShape(4.dp, 8.dp, 8.dp, 4.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    VerticalDivider(
                        thickness = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        text = stringResource(id = R.string.language_note),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            if (languagesState().loading) {
                items(20) {
                    CustomListItem(
                        enabled = false,
                        onClick = {},
                        leadingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_radio_button_unchecked_fill0_wght400),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        },
                        headlineContent = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.extraSmall)
                                        .shimmerEffect(),
                                    text = "Language original name",
                                    color = Color.Transparent
                                )
                                Text(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.extraSmall)
                                        .shimmerEffect(),
                                    text = "Language name",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Transparent
                                )
                            }
                        }
                    )
                }
            } else {
                languagesState().errorMessage?.let { /*TODO*/ }
                languagesState().languages?.let { languages ->
                    if (languages.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                text = stringResource(id = R.string.empty_languages),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(
                            items = languages,
                            key = { it.languageCode },
                            contentType = { "LanguageCode" }
                        ) { languageInfo ->
                            CustomListItem(
                                onClick = { languagesUiEvent(LanguagesUiEvent.SetLanguage(languageInfo.languageCode)) },
                                leadingContent = {
                                    AnimatedContent(
                                        targetState = languagesState().selectedLanguage == languageInfo.languageCode,
                                        label = "Icon change animation"
                                    ) { selected ->
                                        if (selected) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.icon_radio_button_checked_fill0_wght400),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(id = R.drawable.icon_radio_button_unchecked_fill0_wght400),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        }
                                    }
                                },
                                headlineContent = {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(text = capitalizeFirstLetter(languageInfo.languageLocale.getDisplayLanguage(languageInfo.languageLocale)))
                                        Text(
                                            text = capitalizeFirstLetter(languageInfo.languageLocale.displayName),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}