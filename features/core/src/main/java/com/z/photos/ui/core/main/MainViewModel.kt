package com.z.photos.ui.core.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z.photos.data.repository.FavoriteChangeNotifier
import com.z.photos.domain.repositories.FavoritesRepository
import com.z.photos.ui.core.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INITIAL_COUNT = 0

@HiltViewModel
class MainViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    private val _favoriteCount = MutableStateFlow(INITIAL_COUNT)
    val favoriteCount: StateFlow<Int> = _favoriteCount

    init {
        loadCount()
        viewModelScope.launch(dispatchers.main) {
            favoriteChangeNotifier.changes.collect {
                loadCount()
            }
        }
    }

    private fun loadCount() {
        viewModelScope.launch(dispatchers.io) {
            _favoriteCount.value = favoritesRepository.getFavoriteCount()
        }
    }
}
