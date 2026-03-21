package com.z.photos.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import com.z.photos.navigation.PHOTO_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PhotoRepository,
) : ViewModel() {

    private val _photoDetailsState = MutableStateFlow<Photo?>(null)
    val photoDetailsState: StateFlow<Photo?> = _photoDetailsState

    init {
        val photoId = checkNotNull(savedStateHandle.get<Long>(PHOTO_ID_ARG))
        viewModelScope.launch(Dispatchers.IO) {
            _photoDetailsState.value = repository.getLocalPhoto(photoId)
        }
    }

    fun toggleFavorite() {
        val current = _photoDetailsState.value ?: return
        val newFavorite = !current.isFavorite
        _photoDetailsState.value = current.copy(isFavorite = newFavorite)
        viewModelScope.launch(Dispatchers.IO) {
            if (newFavorite) {
                repository.favoritePhoto(current.id)
            } else {
                repository.unfavoritePhoto(current.id)
            }
        }
    }
}
