package com.personal.tmdb.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MediaGrid(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    columns: GridCells = GridCells.Adaptive(100.dp),
    loadMoreItems: () -> Unit = {},
    span: (LazyGridScope.() -> Unit)? = null,
    items: LazyGridScope.() -> Unit
) {
    val reachingEnd by remember {
        derivedStateOf {
            val visibleItems = lazyGridState.layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                val lastVisibleItemIndex = visibleItems.last().index
                val totalItemsCount = lazyGridState.layoutInfo.totalItemsCount
                lastVisibleItemIndex >= totalItemsCount - 10
            } else {
                false
            }
        }
    }
    LaunchedEffect(key1 = reachingEnd) {
        if (reachingEnd) {
            loadMoreItems()
        }
    }
    LazyVerticalGrid(
        modifier = modifier,
        columns = columns,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyGridState
    ) {
        span?.let {
            it()
        }
        items()
    }
}