package com.personal.tmdb.core.domain.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class DialogEvent(
    @DrawableRes val iconRes: Int? = null,
    val title: UiText? = null,
    val message: UiText,
    val dismissAction: DialogAction? = null,
    val confirmAction: DialogAction
)

data class DialogAction(
    val name: UiText,
    val action: () -> Unit,
    val color: Color = Color.Unspecified
)

object DialogController {

    private val _events = Channel<DialogEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: DialogEvent) {
        _events.send(event)
    }
}