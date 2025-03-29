package com.personal.tmdb.settings.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.capitalizeFirstLetter
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.settings.presentation.settings.SettingsUiEvent
import java.util.Locale

@Composable
fun AppSettings(
    modifier: Modifier = Modifier,
    preferencesState: () -> PreferencesState,
    settingsUiEvent: (SettingsUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.app_settings),
            style = MaterialTheme.typography.titleMedium
        )
        CustomListItem(
            onClick = { settingsUiEvent(SettingsUiEvent.OnNavigateTo(Route.Appearance)) },
            leadingContent = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_palette_fill0_wght400),
                    contentDescription = null
                )
            },
            headlineContent = {
                Text(text = stringResource(id = R.string.appearance))
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        )
        CustomListItem(
            onClick = { settingsUiEvent(SettingsUiEvent.OnNavigateTo(Route.Language)) },
            leadingContent = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_language_korean_latin_fill0_wght400),
                    contentDescription = null
                )
            },
            headlineContent = {
                Text(text = stringResource(id = R.string.language))
            },
            trailingContent = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = capitalizeFirstLetter(Locale(preferencesState().language).displayName))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        )
    }
}