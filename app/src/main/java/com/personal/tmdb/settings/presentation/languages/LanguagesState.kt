package com.personal.tmdb.settings.presentation.languages

import com.personal.tmdb.core.domain.util.LanguageCode
import com.personal.tmdb.core.domain.util.UiText
import com.personal.tmdb.settings.data.models.LanguageInfo

data class LanguagesState(
    val selectedLanguage: LanguageCode? = null,
    val languages: List<LanguageInfo>? = null,
    val loading: Boolean = false,
    val errorMessage: UiText? = null
)
