package com.personal.tmdb.profile.presentation.lists.presentation.lists.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.shimmerEffect
import com.personal.tmdb.core.presentation.components.ListItemShimmer

@Suppress("functionName")
fun LazyGridScope.ListScreenShimmer() {
    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateItem()
                .clip(MaterialTheme.shapes.large)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                    shape = MaterialTheme.shapes.large
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.create_list),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
    items(
        count = 4,
        contentType = { "List" }
    ) {
        ListItemShimmer(
            modifier = Modifier.fillMaxWidth(),
            height = Dp.Unspecified
        )
    }
}