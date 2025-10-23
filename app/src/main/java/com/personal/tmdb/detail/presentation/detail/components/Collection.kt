package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.domain.util.TMDB
import com.personal.tmdb.core.domain.util.clickableWithOpaque
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.detail.domain.models.BelongsToCollection
import com.personal.tmdb.detail.presentation.detail.DetailUiEvent

@Composable
fun Collection(
    modifier: Modifier = Modifier,
    collection: BelongsToCollection,
    detailUiEvent: (DetailUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.collection),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .clickableWithOpaque(
                    color = MaterialTheme.colorScheme.surface
                ) {
                    detailUiEvent(DetailUiEvent.OnNavigateTo(Route.Collection(collection.id)))
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(100.dp)
                    .aspectRatio(16 / 9f)
                    .clip(MaterialTheme.shapes.medium),
                model = TMDB.backdropW1280(collection.backdropPath),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(id = R.string.part_of_collection, collection.name ?: ""),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}