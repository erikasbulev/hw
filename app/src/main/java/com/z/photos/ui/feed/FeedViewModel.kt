package com.z.photos.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import com.z.photos.pager.PaginationMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val paginationMediator: PaginationMediator,
    private val repository: PhotoRepository,
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    init {
        viewModelScope.launch {
            paginationMediator.getPhotosFlow()
                .collect { newPage ->
                    _photos.update { current -> current + newPage }
                }
        }
        paginationMediator.requestMorePhotos()
    }

    fun onRequestMoreItems() {
        paginationMediator.requestMorePhotos()
    }

    fun toggleFavorite(photo: Photo) {
        val newIsFavorite = !photo.isFavorite
        _photos.update { current ->
            current.map { if (it.id == photo.id) it.copy(isFavorite = newIsFavorite) else it }
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (newIsFavorite) {
                repository.favoritePhoto(photo.id)
            } else {
                repository.unfavoritePhoto(photo.id)
            }
        }
    }
}
