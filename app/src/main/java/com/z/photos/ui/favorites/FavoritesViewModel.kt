package com.z.photos.ui.favorites

import androidx.lifecycle.ViewModel
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import com.z.photos.ui.FavoriteChangeNotifier
import com.z.photos.ui.launchOnIO
import com.z.photos.ui.launchOnMain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: PhotoRepository,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Photo>>(emptyList())
    val favorites: StateFlow<List<Photo>> = _favorites

    init {
        loadFavorites()
        launchOnMain {
            favoriteChangeNotifier.changes.collect {
                loadFavorites()
            }
        }
    }

    fun unfavorite(photo: Photo) {
        launchOnIO {
            repository.unfavoritePhoto(photo.id)
        }
    }

    private fun loadFavorites() {
        launchOnIO {
            _favorites.value = repository.getFavoritePhotos()
        }
    }
}
