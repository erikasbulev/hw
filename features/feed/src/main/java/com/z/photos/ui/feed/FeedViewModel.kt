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

private const val FIRST_PAGE = 1

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val paginationMediator: PaginationMediator,
    private val repository: PhotoRepository,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState

    init {
        launchOnMain {
            paginationMediator.getPhotosFlow()
                .collect { state ->
                    _uiState.update { current ->
                        current.copy(
                            photos = current.photos + state.photos,
                            hasMore = state.hasMore,
                            nextPage = state.nextPage,
                            isRefreshing = false,
                        )
                    }
                }
        }
        launchOnMain {
            favoriteChangeNotifier.changes.collect {
                refreshFavorites()
            }
        }
        paginationMediator.requestPage(FIRST_PAGE)
    }

    fun onRequestMoreItems() {
        if (!_uiState.value.hasMore) return
        paginationMediator.requestPage(_uiState.value.nextPage)
    }

    fun onRefresh() {
        _uiState.update {
            it.copy(
                photos = emptyList(),
                hasMore = true,
                nextPage = FIRST_PAGE,
                isRefreshing = true,
            )
        }
        launchOnIO {
            paginationMediator.refresh()
        }
    }

    fun toggleFavorite(photo: Photo) {
        launchOnIO {
            val newIsFavorite = !photo.isFavorite
            if (newIsFavorite) {
                repository.favoritePhoto(photo.id)
            } else {
                repository.unfavoritePhoto(photo.id)
            }
            _uiState.update { current ->
                current.copy(
                    photos = current.photos.map {
                        if (it.id == photo.id) it.copy(isFavorite = newIsFavorite) else it
                    }
                )
            }
        }
    }

    private fun refreshFavorites() {
        launchOnIO {
            val favoriteIds = repository.getFavoritePhotos().map { it.id }.toSet()
            _uiState.update { current ->
                current.copy(
                    photos = current.photos.map {
                        it.copy(isFavorite = it.id in favoriteIds)
                    }
                )
            }
        }
    }

    data class FeedUiState(
        val photos: List<Photo> = emptyList(),
        val isRefreshing: Boolean = false,
        val hasMore: Boolean = true,
        val nextPage: Int = FIRST_PAGE,
    )
}
