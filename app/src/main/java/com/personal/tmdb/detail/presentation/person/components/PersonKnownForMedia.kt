package com.personal.tmdb.detail.presentation.person.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.core.navigation.Route
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.MediaType
import com.personal.tmdb.core.domain.util.formatPersonActing
import com.personal.tmdb.detail.domain.models.CombinedCastCrewInfo

@Composable
fun PersonKnownForMedia(
    modifier: Modifier = Modifier,
    onNavigateTo: (route: Route) -> Unit,
    info: () -> CombinedCastCrewInfo
) {
    Row(
        modifier = modifier
            .clickable {
                when (info().mediaType) {
                    MediaType.TV, MediaType.MOVIE -> {
                        onNavigateTo(
                            Route.Detail(
                                mediaType = info().mediaType.name.lowercase(),
                                mediaId = info().id
                            )
                        )
                    }
                    else -> {
                        onNavigateTo(Route.Lost)
                    }
                }
            }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.widthIn(35.dp),
                text = info().releaseDate?.year?.toString() ?: stringResource(id = R.string.dash),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            AsyncImage(
                modifier = Modifier
                    .height(80.dp)
                    .aspectRatio(2 / 3f)
                    .clip(MaterialTheme.shapes.small),
                model = C.TMDB_IMAGES_BASE_URL + C.POSTER_W154 + info().posterPath,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
                contentDescription = "Poster",
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            info().name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Column {
                info().jobs?.fastForEach { jobs ->
                    formatPersonActing(
                        numberOfEpisodes = jobs.episodeCount,
                        character = jobs.character,
                        job = jobs.job
                    ).takeIf { it.isNotEmpty() }?.let { annotatedString ->
                        Text(
                            text = annotatedString,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}