package com.personal.tmdb.discover.data.repository

import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.discover.data.mappers.toCountry
import com.personal.tmdb.discover.data.mappers.toLanguage
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.models.Language
import com.personal.tmdb.discover.domain.repository.DiscoverRepository
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(
    private val tmdbApi: TmdbApi
): DiscoverRepository {

    override suspend fun getCountries(): Result<List<Country>, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getCountries().toCountry()
        }
    }

    override suspend fun getLanguages(): Result<List<Language>, DataError.Remote> {
        return safeApiCall {
            tmdbApi.getLanguages().toLanguage()
        }
    }
}