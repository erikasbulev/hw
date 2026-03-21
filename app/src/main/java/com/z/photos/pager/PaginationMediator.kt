package com.z.photos.pager

import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
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

@OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)
class PaginationMediator @Inject constructor(
    private val photoRepository: PhotoRepository,
) {

    private val page = AtomicInt(1)
    private val pageRequests = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1
    )


    fun getPhotosFlow(): Flow<List<Photo>> {
        return pageRequests.flatMapConcat { page ->
            flow {
                val local = photoRepository.getLocalPhotos(page)
                if (local.isNotEmpty()) {
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

    fun resetPagination() {
        page.store(1)
    }
}