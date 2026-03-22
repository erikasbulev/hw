package com.z.photos.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
import com.z.photos.ui.core.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PHOTO_ID_ARG = "photoId"

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PhotoRepository,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    private val _photoDetailsState = MutableStateFlow<Photo?>(null)
    val photoDetailsState: StateFlow<Photo?> = _photoDetailsState

    init {
        val photoId = checkNotNull(savedStateHandle.get<Long>(PHOTO_ID_ARG))
        viewModelScope.launch(dispatchers.io) {
            _photoDetailsState.value = repository.getPhoto(photoId)
        }
    }

    fun toggleFavorite() {
        val current = _photoDetailsState.value ?: return
        val newFavorite = !current.isFavorite
        _photoDetailsState.value = current.copy(isFavorite = newFavorite)
        viewModelScope.launch(dispatchers.main) {
            if (newFavorite) {
                repository.favoritePhoto(current.id)
            } else {
                repository.unfavoritePhoto(current.id)
            }
        }
    }
}
