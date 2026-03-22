package com.z.photos.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z.photos.data.repository.FavoriteChangeNotifier
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.FavoritesRepository
import com.z.photos.ui.core.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Photo>>(emptyList())
    val favorites: StateFlow<List<Photo>> = _favorites

    init {
        loadFavorites()
        viewModelScope.launch(dispatchers.main) {
            favoriteChangeNotifier.changes.collect {
                loadFavorites()
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch(dispatchers.io) {
            _favorites.value = favoritesRepository.getFavoritePhotos()
        }
    }

    fun unfavorite(photo: Photo) {
        viewModelScope.launch(dispatchers.io) {
            favoritesRepository.unfavoritePhoto(photo.id)
        }
    }
}
