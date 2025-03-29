package com.personal.tmdb.core.presentation.welcome_back

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.models.User
import com.personal.tmdb.core.domain.util.C

@Composable
fun WelcomeBackScreenRoot(
    onNavigateBack: () -> Unit,
    userState: () -> UserState
) {
    WelcomeBackScreen(
        onNavigateBack = onNavigateBack,
        userState = userState
    )
}

@Composable
private fun WelcomeBackScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    userState: () -> UserState = { UserState(user = User(name = "John Doe")) }
) {
    val animatedGradientColor by animateColorAsState(
        targetValue = userState().dominantColor?.color ?: MaterialTheme.colorScheme.secondary,
        label = "Animated gradient color"
    )
    AnimatedContent(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .background(
                Brush.verticalGradient(
                    listOf(
                        animatedGradientColor.copy(alpha = .3f),
                        Color.Transparent
                    )
                )
            ),
        targetState = userState().loading,
        label = ""
    ) { loading ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .safeDrawingPadding(),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 200.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        trackColor = Color.Transparent,
                        strokeWidth = 3.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
            } else {
                userState().user?.let { user ->
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.widthIn(max = 360.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(180.dp)
                                        .clip(CircleShape),
                                    model = if (user.tmdbAvatarPath == null) {
                                        C.GRAVATAR_IMAGES_BASE_URL.format(userState().user?.gravatarAvatarPath)
                                    } else {
                                        C.TMDB_IMAGES_BASE_URL + C.PROFILE_W185 + userState().user?.tmdbAvatarPath
                                    },
                                    placeholder = painterResource(R.drawable.placeholder),
                                    error = painterResource(R.drawable.placeholder),
                                    contentDescription = stringResource(R.string.me),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        append("${stringResource(id = R.string.welcome_back)}, ")
                                        withStyle(
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                fontWeight = FontWeight.Medium,
                                                fontStyle = FontStyle.Italic
                                            ).toSpanStyle()
                                        ) {
                                            append(user.name?.takeIf { it.isNotEmpty() } ?: user.username?.takeIf { it.isNotEmpty() } ?: " Who are you again??")
                                        }
                                    },
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Italic,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.now_you_can),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(MaterialTheme.shapes.extraLarge)
                                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(28.dp),
                                            painter = painterResource(id = R.drawable.icon_star_shine_fill1_wght400),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = stringResource(id = R.string.rate_feature),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(MaterialTheme.shapes.extraLarge)
                                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(28.dp),
                                            painter = painterResource(id = R.drawable.icon_book_4_spark_fill1_wght400),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = stringResource(id = R.string.lists_feature),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(MaterialTheme.shapes.extraLarge)
                                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = .05f))
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(28.dp),
                                            painter = painterResource(id = R.drawable.icon_local_fire_department_fill1_wght400),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = stringResource(id = R.string.recommendations_feature),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                        ) {
                            Button(
                                onClick = { onNavigateBack() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = animatedGradientColor,
                                    contentColor = userState().dominantColor?.onColor ?: MaterialTheme.colorScheme.surface
                                ),
                                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.cool),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                } ?: Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                    ) {
                        Text(
                            text = userState().errorMessage?.asString() ?: stringResource(id = R.string.error_unknown),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Button(
                            onClick = { onNavigateBack() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.surface
                            ),
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.take_me_back),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}