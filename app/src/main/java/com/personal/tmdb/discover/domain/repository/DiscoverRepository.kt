package com.personal.tmdb.discover.domain.repository

import androidx.paging.Pager
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.detail.domain.models.GenresInfo
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.models.Language

interface DiscoverRepository {

    suspend fun getCountries(): Result<List<Country>, DataError.Remote>

    suspend fun getLanguages(): Result<List<Language>, DataError.Remote>

    suspend fun getPopularPeople(language: String? = null): Pager<Int, MediaInfo>

    suspend fun getGenres(mediaType: String, language: String?): Result<GenresInfo, DataError.Remote>

    suspend fun discoverMedia(
        mediaType: String,
        language: String? = null,
        includeAdult: Boolean,
        airDateYear: String,
        fromAirDate: String,
        toAirDate: String,
        sortBy: String,
        fromRating: Float,
        toRating: Float,
        minRatingCount: Float,
        withGenre: String,
        withOriginCountry: String,
        withOriginalLanguage: String,
        fromRuntime: Int,
        toRuntime: Int
    ): Pager<Int, MediaInfo>
}