package com.personal.tmdb.settings.data.repository

import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.settings.data.mappers.toLanguageInfo
import com.personal.tmdb.settings.data.models.LanguageInfo
import com.personal.tmdb.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): SettingsRepository {

    override suspend fun getPrimaryTranslations(): Result<List<LanguageInfo>, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getPrimaryTranslations().toLanguageInfo()
        }
    }
}