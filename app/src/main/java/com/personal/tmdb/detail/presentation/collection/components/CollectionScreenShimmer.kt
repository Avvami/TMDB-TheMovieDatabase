package com.personal.tmdb.detail.presentation.collection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.core.presentation.PreferencesState
import com.personal.tmdb.core.presentation.components.MediaGrid
import com.personal.tmdb.core.presentation.components.MediaPosterShimmer
import com.personal.tmdb.core.domain.util.shimmerEffect

@Composable
fun CollectionScreenShimmer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    preferencesState: () -> PreferencesState
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
        LocalContentColor provides Color.Transparent
    ) {
        MediaGrid(
            modifier = modifier,
            contentPadding = contentPadding,
            span = {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Column(
                        modifier = Modifier.padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .shimmerEffect(),
                            text = "Collection Name",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .shimmerEffect()
                                .fillMaxWidth(),
                            text = "",
                            style = MaterialTheme.typography.bodyLarge,
                            minLines = 4
                        )
                        Row(
                            modifier = modifier,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .shimmerEffect(),
                                enabled = false,
                                onClick = {},
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = Color.Transparent
                                )
                            ) {
                                Text(text = stringResource(id = R.string.amount_of_movies, 10))
                            }
                            Button(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .shimmerEffect(),
                                enabled = false,
                                onClick = {},
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = Color.Transparent
                                )
                            ) {
                                Icon(
                                    modifier = Modifier.size(ButtonDefaults.IconSize),
                                    painter = painterResource(id = R.drawable.icon_thumbs_up_down_fill1_wght400),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                                Text(text = "10")
                            }
                        }
                        SuggestionChip(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .shimmerEffect(),
                            enabled = false,
                            onClick = {},
                            icon = {
                                Icon(
                                    modifier = Modifier.size(SuggestionChipDefaults.IconSize),
                                    painter = painterResource(id = R.drawable.icon_page_info_fill0_wght400),
                                    contentDescription = "Sort by"
                                )
                            },
                            label = {
                                Text(text = stringResource(id = R.string.sort_by))
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                disabledContainerColor = Color.Transparent,
                                disabledIconContentColor = Color.Transparent,
                                disabledLabelColor = Color.Transparent
                            ),
                            border = null
                        )
                    }
                }
            },
            items = {
                items(
                    count = 4,
                    contentType = { "Poster" }
                ) {
                    MediaPosterShimmer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        height = Dp.Unspecified,
                        showTitle = preferencesState().showTitle,
                    )
                }
            }
        )
    }
}