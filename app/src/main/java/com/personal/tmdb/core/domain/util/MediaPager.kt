package com.personal.tmdb.core.domain.util

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource

fun <Key : Any, Value : Any> mediaPager(
    pageSize: Int = 20,
    prefetchDistance: Int = 1,
    enablePlaceholders: Boolean = false,
    initialLoadSize: Int = pageSize,
    pagingSourceFactory: () -> PagingSource<Key, Value>
): Pager<Key, Value> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            prefetchDistance = prefetchDistance,
            enablePlaceholders = enablePlaceholders,
            initialLoadSize = initialLoadSize
        ),
        pagingSourceFactory = pagingSourceFactory
    )
}