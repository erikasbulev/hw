package com.z.photos.ui.feed

import androidx.lifecycle.ViewModel
import com.z.photos.data.repository.FavoriteChangeNotifier
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
import com.z.photos.ui.core.launchOnIO
import com.z.photos.ui.core.launchOnMain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val paginationMediator: PaginationMediator,
    private val repository: PhotoRepository,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    init {
        launchOnMain {
            paginationMediator.getPhotosFlow()
                .collect { newPage ->
                    _photos.update { current -> current + newPage }
                }
        }
        launchOnMain {
            favoriteChangeNotifier.changes.collect {
                refreshFavorites()
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
        launchOnIO {
            if (newIsFavorite) {
                repository.favoritePhoto(photo.id)
            } else {
                repository.unfavoritePhoto(photo.id)
            }
        }
    }

    private fun refreshFavorites() {
        launchOnIO {
            val favoriteIds = repository.getFavoritePhotos().map { it.id }.toSet()
            _photos.update { current ->
                current.map { it.copy(isFavorite = it.id in favoriteIds) }
            }
        }
    }
}
