package com.personal.tmdb.profile.presentation.lists.presentation.list_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.presentation.components.CustomListItem
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.ListDetailsState
import com.personal.tmdb.profile.presentation.lists.presentation.list_details.ListDetailsUiEvent

@Composable
fun EditList(
    modifier: Modifier = Modifier,
    listDetailsState: () -> ListDetailsState,
    listDetailsUiEvent: (ListDetailsUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomListItem(
            enabled = false,
            onClick = {},
            contentPadding = PaddingValues(),
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.is_public_list)
                )
            },
            leadingContent = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_public_fill0_wght400),
                    contentDescription = null
                )
            },
            trailingContent = {
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                ) {
                    Switch(
                        checked = listDetailsState().publicList,
                        onCheckedChange = { listDetailsUiEvent(ListDetailsUiEvent.SetListVisibility(!listDetailsState().publicList)) },
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                            uncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = listDetailsState().listName,
            onValueChange = {
                listDetailsUiEvent(ListDetailsUiEvent.SetListName(it))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.list_name_placeholder))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = listDetailsState().listDescription,
            onValueChange = {
                listDetailsUiEvent(ListDetailsUiEvent.SetListDescription(it))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.list_description_placeholder))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
        ) {
            TextButton(
                modifier = Modifier.width(200.dp),
                onClick = { listDetailsUiEvent(ListDetailsUiEvent.DeleteList) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = stringResource(id = R.string.delete_list))
            }
        }
    }
}