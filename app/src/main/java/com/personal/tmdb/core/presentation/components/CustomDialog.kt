package com.personal.tmdb.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.personal.tmdb.core.domain.util.DialogEvent

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    dialogState: DialogEvent,
    onDismissRequest: () -> Unit,
    showDialog: Boolean = false
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                dialogState.iconRes?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = it),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                dialogState.title?.let { title ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = title.asString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = dialogState.message.asString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        dialogState.dismissAction?.let { action ->
                            TextButton(
                                onClick = {
                                    action.action.invoke()
                                    onDismissRequest()
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = if (action.color == Color.Unspecified) MaterialTheme.colorScheme.primary else
                                        action.color
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(text = action.name.asString())
                            }
                        }
                        TextButton(
                            onClick = {
                                dialogState.confirmAction.action.invoke()
                                onDismissRequest()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (dialogState.confirmAction.color == Color.Unspecified) MaterialTheme.colorScheme.primary else
                                    dialogState.confirmAction.color
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(text = dialogState.confirmAction.name.asString())
                        }
                    }
                }
            }
        }
    }
}