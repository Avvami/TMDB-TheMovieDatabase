package com.personal.tmdb.settings.domain.repository

import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.settings.data.models.LanguageInfo

interface SettingsRepository {

    suspend fun getPrimaryTranslations(): Result<List<LanguageInfo>, DataError.Remote>
}