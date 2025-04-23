package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.detail.presentation.detail.DetailState

@Composable
fun DetailAll(
    modifier: Modifier = Modifier,
    detailState: () -> DetailState,
    userState: () -> UserState
) {
    detailState().details?.let { details ->
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
        ) {
            details.tagline?.let { tagline ->
                item {
                    Text(
                        text = tagline,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            details.overview?.let { overview ->
                item {
                    Text(text = overview)
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    details.originalName?.takeIf { it != details.name }?.let { originalName ->
                        AnnotatedListText(
                            titlePrefix = stringResource(id = R.string.original_title),
                            items = listOf(originalName)
                        )
                    }
                    details.originalLanguage?.let { originalLanguage ->
                        AnnotatedListText(
                            titlePrefix = stringResource(id = R.string.original_language),
                            items = listOf(originalLanguage)
                        )
                    }
                    details.status?.let { status ->
                        AnnotatedListText(
                            titlePrefix = stringResource(id = R.string.status),
                            items = listOf(status)
                        )
                    }
                    details.networks?.let { networks ->
                        AnnotatedListText(
                            titlePrefix = stringResource(id = if (networks.size == 1) R.string.network else R.string.networks),
                            items = networks.map { it.name ?: stringResource(id = R.string.no_name) }
                        )
                    }
                    details.productionCompanies?.let { productionCompanies ->
                        AnnotatedListText(
                            titlePrefix = stringResource(id = if (productionCompanies.size == 1) R.string.production_company else R.string.production_companies),
                            items = productionCompanies.map { it.name ?: stringResource(id = R.string.no_name) }
                        )
                    }
                }
            }
            item {
                val userCountryCode = userState().user?.iso31661 ?: "US"
                val contentRating = remember {
                    when (detailState().mediaType) {
                        MediaType.TV -> {
                            details.contentRatings?.contentRatingsResults?.find { it.iso31661 == userCountryCode }?.rating?.takeIf { it.isNotEmpty() }
                        }
                        MediaType.MOVIE -> {
                            details.releaseDates?.releaseDatesResults?.find { it.iso31661 == userCountryCode }?.releaseDates
                                ?.find { it.certification.isNotEmpty() }?.certification?.takeIf { it.isNotEmpty() }
                        }
                        else -> null
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    contentRating?.let { contentRating ->
                        Text(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            text = contentRating,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                    details.originCountry?.let { originCountry ->
                        Text(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            text = originCountry.take(3).joinToString(", ") { it },
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }
    }
}