package com.personal.tmdb.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.personal.tmdb.core.domain.models.MediaInfo
import com.personal.tmdb.core.domain.models.MediaResponseInfo
import com.personal.tmdb.core.domain.util.DataError
import com.personal.tmdb.core.domain.util.Result
import com.personal.tmdb.core.domain.util.UiTextException
import com.personal.tmdb.core.domain.util.fold
import com.personal.tmdb.core.domain.util.toUiText

class MediaPagingSource(
    private val loadPage: suspend (page: Int) -> Result<MediaResponseInfo, DataError.Remote>,
    private val maxPage: Int? = null
): PagingSource<Int, MediaInfo>() {

    override fun getRefreshKey(state: PagingState<Int, MediaInfo>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaInfo> {
        val page = params.key ?: 1
        return loadPage(page).fold(
            onSuccess = { result ->
                val  totalPages = maxPage?.let { minOf(it, result.totalPages) } ?: result.totalPages
                LoadResult.Page(
                    data = result.results,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page >= totalPages) null else page + 1
                )
            },
            onError = { error ->
                LoadResult.Error(UiTextException(error.toUiText()))
            }
        )
    }
}