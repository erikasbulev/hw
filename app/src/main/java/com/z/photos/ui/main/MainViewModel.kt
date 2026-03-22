package com.z.photos.ui.main

import androidx.lifecycle.ViewModel
import com.z.photos.business.repositories.PhotoRepository
import com.z.photos.ui.FavoriteChangeNotifier
import com.z.photos.ui.launchOnMain
import com.z.photos.ui.launchOnIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

private const val INITIAL_COUNT = 0

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PhotoRepository,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
) : ViewModel() {

    private val _favoriteCount = MutableStateFlow(INITIAL_COUNT)
    val favoriteCount: StateFlow<Int> = _favoriteCount

    init {
        loadCount()
        launchOnMain {
            favoriteChangeNotifier.changes.collect {
                loadCount()
            }
        }
    }

    private fun loadCount() {
        launchOnIO {
            _favoriteCount.value = repository.getFavoriteCount()
        }
    }
}
