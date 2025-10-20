package com.personal.tmdb.detail.presentation.reviews.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.presentation.components.HtmlTextContainer
import com.personal.tmdb.core.domain.util.formatDate
import com.personal.tmdb.detail.domain.models.Review

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Review(
    modifier: Modifier = Modifier,
    review: Review
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        with(review) {
            buildList<@Composable FlowRowScope.() -> Unit> {
                authorDetails?.rating?.let { rating ->
                    add {
                        Row(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                                contentDescription = "Thumbs",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = rating.toString(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                author?.let { author ->
                    add {
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = author,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                createdAt?.let { date ->
                    add {
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = formatDate(date),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }.takeIf { it.isNotEmpty() }?.let { components ->
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    components.forEachIndexed { index, component ->
                        component()
                        if (index != components.lastIndex) {
                            Icon(
                                modifier = Modifier
                                    .size(6.dp)
                                    .align(Alignment.CenterVertically),
                                painter = painterResource(id = R.drawable.icon_fiber_manual_record_fill1_wght400),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
            content?.let { content ->
                HtmlTextContainer(
                    text = content
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        maxLines = 10,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReviewShimmer(
    modifier: Modifier = Modifier
) {
    val minLines = remember { (1..8).random() }
    CompositionLocalProvider(
        LocalContentColor provides Color.Transparent
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                        contentDescription = "Thumbs",
                    )
                    Text(
                        text = "10",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(6.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.icon_fiber_manual_record_fill1_wght400),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Username",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    modifier = Modifier
                        .size(6.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.icon_fiber_manual_record_fill1_wght400),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Jan 1, 1111",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    modifier = Modifier
                        .size(6.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.icon_fiber_manual_record_fill1_wght400),
                    contentDescription = null,
                )
            }
            Text(
                text = "",
                style = MaterialTheme.typography.bodyLarge,
                minLines = minLines
            )
        }
    }
}