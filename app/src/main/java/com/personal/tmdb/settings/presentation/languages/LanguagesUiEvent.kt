package com.personal.tmdb.settings.presentation.languages

sealed interface LanguagesUiEvent {
    data object OnNavigateBack: LanguagesUiEvent
    data class SetLanguage(val languageCode: String): LanguagesUiEvent
}