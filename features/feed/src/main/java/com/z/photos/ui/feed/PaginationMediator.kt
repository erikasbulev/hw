package com.z.photos.ui.feed

import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch

private const val FIRST_PAGE = 1
private const val PAGE_BUFFER_CAPACITY = 1

@OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)
class PaginationMediator @Inject constructor(
    private val photoRepository: PhotoRepository,
) {

    private val page = AtomicInt(FIRST_PAGE)
    private val pageRequests = MutableSharedFlow<Int>(
        replay = PAGE_BUFFER_CAPACITY,
        extraBufferCapacity = PAGE_BUFFER_CAPACITY
    )

    fun getPhotosFlow(): Flow<List<Photo>> {
        return pageRequests.flatMapConcat { page ->
            flow {
                val local = photoRepository.getLocalPhotos(page)
                if (local.isNotEmpty() && !photoRepository.isCacheStale(page)) {
                    emit(local)
                } else {
                    val remote = photoRepository.getRemotePhotos(page)
                    photoRepository.savePhotos(page, remote)

                    val saved = photoRepository.getLocalPhotos(page)
                    emit(saved)
                }
            }.flowOn(Dispatchers.IO)
        }.onEach {
            page.incrementAndFetch()
        }
    }

    fun requestMorePhotos() {
        pageRequests.tryEmit(page.load())
    }

    suspend fun refresh() {
        photoRepository.clearPhotos()
        page.store(FIRST_PAGE)
        pageRequests.emit(FIRST_PAGE)
    }
}
