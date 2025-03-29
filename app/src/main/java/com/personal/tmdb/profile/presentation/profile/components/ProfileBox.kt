package com.personal.tmdb.profile.presentation.profile.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.personal.tmdb.MainActivity
import com.personal.tmdb.R
import com.personal.tmdb.UserState
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.findActivity
import com.personal.tmdb.core.presentation.components.AutoResizedText
import com.personal.tmdb.profile.presentation.profile.ProfileUiEvent
import com.personal.tmdb.ui.theme.surfaceLight
import com.personal.tmdb.ui.theme.tmdbRadialDarkPurple
import com.personal.tmdb.ui.theme.tmdbRed

@Composable
fun ProfileBox(
    modifier: Modifier = Modifier,
    userState: () -> UserState,
    profileUiEvent: (ProfileUiEvent) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = !userState().user?.sessionId.isNullOrEmpty(),
        label = "User box"
    ) { userSignedIn ->
        if (userSignedIn) {
            SignedInUser(
                userState = userState
            )
        } else {
            SignedOutUser(
                userState = userState,
                profileUiEvent = profileUiEvent
            )
        }
    }
}

@Composable
fun SignedInUser(
    modifier: Modifier = Modifier,
    userState: () -> UserState
) {
    val animatedGradientColor by animateColorAsState(
        targetValue = userState().dominantColor?.color ?: MaterialTheme.colorScheme.secondary,
        label = "Animated gradient color"
    )
    val radialGradient by remember(animatedGradientColor) {
        mutableStateOf(
            object : ShaderBrush() {
                override fun createShader(size: Size): Shader {
                    val biggerDimension = maxOf(size.height, size.width)
                    return RadialGradientShader(
                        colors = listOf(animatedGradientColor.copy(alpha = .3f), Color.Transparent),
                        center = Offset.Zero,
                        radius = biggerDimension
                    )
                }
            }
        )
    }
    Row(
        modifier = modifier
            .background(radialGradient)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (userState().user?.tmdbAvatarPath == null) {
                C.GRAVATAR_IMAGES_BASE_URL.format(userState().user?.gravatarAvatarPath)
            } else {
                C.TMDB_IMAGES_BASE_URL + C.PROFILE_W185 + userState().user?.tmdbAvatarPath
            },
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
            contentDescription = stringResource(R.string.me),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (userState().user?.name.isNullOrEmpty()) {
                Text(
                    text = userState().user?.username ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = userState().user?.name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = userState().user?.username ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
fun SignedOutUser(
    modifier: Modifier = Modifier,
    userState: () -> UserState,
    profileUiEvent: (ProfileUiEvent) -> Unit
) {
    val activity = LocalContext.current.findActivity() as MainActivity
    var showLoadingDialog by remember { mutableStateOf(false) }

    if (showLoadingDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnClickOutside = !userState().loading,
                dismissOnBackPress = !userState().loading
            ),
            onDismissRequest = { showLoadingDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (userState().errorMessage == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = tmdbRed,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.icon_running_with_errors_fill0_wght400),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                userState().errorMessage?.let { message ->
                    Text(
                        text = message.asString(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = userState().requestToken, key2 = userState().loading) {
        if (userState().requestToken != null && !userState().loading) {
            showLoadingDialog = false
            activity.openCustomChromeTab(
                url = "https://www.themoviedb.org/auth/access?request_token=${userState().requestToken}"
            )
            profileUiEvent(ProfileUiEvent.DropRequestToken)
        }
    }

    val radialGradient by remember {
        mutableStateOf(
            object : ShaderBrush() {
                override fun createShader(size: Size): Shader {
                    val biggerDimension = maxOf(size.height, size.width)
                    return RadialGradientShader(
                        colors = listOf(tmdbRadialDarkPurple, tmdbRadialDarkPurple.copy(alpha = .3f)),
                        center = Offset.Zero,
                        radius = biggerDimension
                    )
                }
            }
        )
    }
    Box(
        modifier = modifier.background(radialGradient)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize(),
            painter = painterResource(id = R.drawable.pipes_red),
            contentDescription = "Red pipes",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 4.dp, end = 24.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                AutoResizedText(
                    text = stringResource(id = R.string.tmdb),
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = surfaceLight,
                        fontSize = 86.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic
                    )
                )
                Image(
                    modifier = Modifier
                        .padding(top = 42.dp)
                        .widthIn(340.dp),
                    painter = painterResource(id = R.drawable.deadpool),
                    contentDescription = "Deadpool"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.widthIn(max = 260.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showLoadingDialog = true
                        profileUiEvent(ProfileUiEvent.CreateRequestToken)
                    },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = surfaceLight,
                        containerColor = surfaceLight.copy(alpha = .1f)
                    )
                ) {
                    Text(text = stringResource(id = R.string.login))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tmdbRed,
                        contentColor = surfaceLight
                    )
                ) {
                    Text(text = stringResource(id = R.string.join_tmdb))
                }
            }
        }
    }
}