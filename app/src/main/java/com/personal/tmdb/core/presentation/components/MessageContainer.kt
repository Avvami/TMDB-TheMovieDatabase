package com.personal.tmdb.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.tmdb.R

@Composable
fun MessageContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium, fontSize = 20.sp)
        ) {
            content()
        }
        onRetry?.let {
            TextButton(
                onClick = it,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(id = R.string.retry),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}