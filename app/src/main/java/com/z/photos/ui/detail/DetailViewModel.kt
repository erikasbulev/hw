package com.z.photos.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import com.z.photos.navigation.PHOTO_ID_ARG
import com.z.photos.ui.launchOnIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        launchOnIO {
            _photoDetailsState.value = repository.getLocalPhoto(photoId)
        }
    }

    fun toggleFavorite() {
        val current = _photoDetailsState.value ?: return
        val newFavorite = !current.isFavorite
        _photoDetailsState.value = current.copy(isFavorite = newFavorite)
        launchOnIO {
            if (newFavorite) {
                repository.favoritePhoto(current.id)
            } else {
                repository.unfavoritePhoto(current.id)
            }
        }
    }
}
