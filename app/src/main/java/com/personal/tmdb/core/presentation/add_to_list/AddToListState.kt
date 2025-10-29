package com.personal.tmdb.core.presentation.add_to_list

import androidx.paging.PagingData
import com.personal.tmdb.core.domain.models.MyList
import kotlinx.coroutines.flow.Flow

data class AddToListState(
    val myListsResults: Flow<PagingData<MyList>>? = null
)
