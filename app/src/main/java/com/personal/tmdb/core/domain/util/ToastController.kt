package com.personal.tmdb.core.domain.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object ToastController {

    private val _events = Channel<UiText>()
    val events = _events.receiveAsFlow()

    suspend fun sendMessage(message: UiText) {
        _events.send(message)
    }
}