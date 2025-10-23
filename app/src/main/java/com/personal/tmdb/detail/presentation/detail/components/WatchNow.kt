package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.clickableNoIndication
import com.personal.tmdb.detail.domain.models.Available
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent
import com.personal.tmdb.detail.presentation.detail.DetailUiState

@Composable
fun WatchNow(
    modifier: Modifier = Modifier,
    available: Available,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickableNoIndication {
                    detailUiEvent(DetailUiEvent.SetUiState(DetailUiState.WATCH_PROVIDERS))
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.watch_now),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(R.drawable.icon_keyboard_arrow_right_fill0_wght400),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val availability = remember {
                when {
                    available.streaming?.firstOrNull() != null -> available.streaming.first() to R.string.now_streaming
                    available.free?.firstOrNull() != null -> available.free.first() to R.string.for_free
                    available.rent?.firstOrNull() != null -> available.rent.first() to R.string.rent_buy_available
                    available.buy?.firstOrNull() != null -> available.buy.first() to R.string.rent_buy_available
                    available.ads?.firstOrNull() != null -> available.ads.first() to R.string.with_ads
                    else -> null
                }
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.small),
                    model = TMDB.logoW92(availability?.first?.logoPath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                availability?.second?.let {
                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified
            ) {
                FilledIconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        detailUiEvent(DetailUiEvent.OpenUrl(available.link))
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .05f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_arrow_outward_fill0_wght400),
                        contentDescription = null
                    )
                }
            }
        }
    }
}