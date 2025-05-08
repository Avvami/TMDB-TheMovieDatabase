package com.personal.tmdb.search.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.personal.tmdb.core.data.mappers.toMediaResponseInfo
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.data.remote.safeApiCall
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.util.UiTextException
import com.personal.tmdb.core.domain.util.fold
import com.personal.tmdb.core.domain.util.toUiText

class SearchPopularPeoplePagingSource(
    private val tmdbApi: TmdbApi,
    private val mediaType: String,
    private val language: String?
): PagingSource<Int, MediaInfo>() {
    override fun getRefreshKey(state: PagingState<Int, MediaInfo>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaInfo> {
        val page = params.key ?: 1
        return safeApiCall {
            tmdbApi.getPopular(mediaType, language, page).toMediaResponseInfo()
        }.fold(
            onSuccess = { result ->
                LoadResult.Page(
                    data = result.results,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page >= 3 || page >= result.totalPages) null else page + 1
                )
            },
            onError = { error ->
                LoadResult.Error(UiTextException(error.toUiText()))
            }
        )
    }
}