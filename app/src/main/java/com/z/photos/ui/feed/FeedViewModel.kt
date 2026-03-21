package com.z.photos.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z.photos.business.entities.Photo
import com.z.photos.pager.PaginationMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val paginationMediator: PaginationMediator,
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    init {
        viewModelScope.launch {
            paginationMediator.getPhotosFlow()
                .collect { newPage ->
                    _photos.update { current ->
                        current + newPage
                    }
                }
        }
        paginationMediator.requestMorePhotos()
    }

    fun onRequestMoreItems() {
        paginationMediator.requestMorePhotos()
    }
}
