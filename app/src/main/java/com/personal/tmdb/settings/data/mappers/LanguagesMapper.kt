package com.personal.tmdb.settings.data.mappers

import com.personal.tmdb.core.data.remote.LanguageCode
import com.personal.tmdb.settings.data.models.LanguageInfo
import java.util.Locale

fun List<LanguageCode>.toLanguageInfo(): List<LanguageInfo> {
    return this
        .map { it.substringBefore("-") }
        .distinct()
        .map { languageCode ->
            val locale = Locale(languageCode)
            LanguageInfo(
                languageCode = languageCode,
                languageLocale = locale
            )
    }
}