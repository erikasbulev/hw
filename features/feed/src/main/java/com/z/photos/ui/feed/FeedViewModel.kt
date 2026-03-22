package com.z.photos.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z.photos.data.repository.FavoriteChangeNotifier
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
import com.z.photos.ui.core.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val FIRST_PAGE = 1

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val paginationMediator: PaginationMediator,
    private val repository: PhotoRepository,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState

    private val _errorEvents = Channel<Unit>(Channel.BUFFERED)
    val errorEvents = _errorEvents.receiveAsFlow()

    init {
        viewModelScope.launch(dispatchers.main) {
            paginationMediator.getPhotosFlow()
                .retryWhen { cause, _ ->
                    if (cause is IOException) {
                        _uiState.update { it.copy(isRefreshing = false) }
                        _errorEvents.send(Unit)
                        true
                    } else {
                        false
                    }
                }
                .collect { state ->
                    _uiState.update { current ->
                        val existingIds = current.photos.map { it.id }.toSet()
                        val newPhotos = state.photos.filter { it.id !in existingIds }
                        current.copy(
                            photos = current.photos + newPhotos,
                            hasMore = state.hasMore,
                            nextPage = state.nextPage,
                            isRefreshing = false,
                            isLoadingMore = false,
                        )
                    }
                }
        }
        viewModelScope.launch(dispatchers.main) {
            favoriteChangeNotifier.changes.collect {
                refreshFavorites()
            }
        }
        paginationMediator.requestPage(FIRST_PAGE)
    }

    fun onRequestMoreItems() {
        val state = _uiState.value
        if (!state.hasMore || state.isLoadingMore) return
        _uiState.update { it.copy(isLoadingMore = true) }
        paginationMediator.requestPage(state.nextPage)
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
        viewModelScope.launch(dispatchers.io) {
            paginationMediator.refresh()
        }
    }

    fun toggleFavorite(photo: Photo) {
        viewModelScope.launch(dispatchers.io) {
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
        viewModelScope.launch(dispatchers.io) {
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
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = true,
        val nextPage: Int = FIRST_PAGE,
    )
}
