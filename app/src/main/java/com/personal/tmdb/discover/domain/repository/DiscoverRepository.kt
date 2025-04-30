package com.personal.tmdb.discover.domain.repository

import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.models.Language

interface DiscoverRepository {

    suspend fun getCountries(): Result<List<Country>, DataError.Remote>

    suspend fun getLanguages(): Result<List<Language>, DataError.Remote>
}