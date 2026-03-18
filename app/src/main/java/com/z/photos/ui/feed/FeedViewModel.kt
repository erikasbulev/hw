package com.z.photos.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.z.photos.ui.feed.paging.FeedPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedPagingSource: FeedPagingSource,
) : ViewModel() {

    val pager = Pager(PagingConfig(pageSize = 20)) { feedPagingSource }
        .flow
        .cachedIn(viewModelScope)

}