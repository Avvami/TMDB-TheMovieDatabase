package com.personal.tmdb

import com.personal.tmdb.core.domain.models.User

sealed interface UiEvent {
    data object CreateRequestToken: UiEvent
    data object DropRequestToken: UiEvent
    data object SignInUser: UiEvent
    data class SignOut(val user: User?): UiEvent
}