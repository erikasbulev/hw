package com.z.photos.data.repository

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val BUFFER_CAPACITY = 1

@Singleton
class FavoriteChangeNotifier @Inject constructor() {

    private val _changes = MutableSharedFlow<Unit>(extraBufferCapacity = BUFFER_CAPACITY)
    val changes: SharedFlow<Unit> = _changes

    fun notifyChange() {
        _changes.tryEmit(Unit)
    }
}
