package com.z.photos.ui.feed

import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
import com.z.photos.ui.core.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val FIRST_PAGE = 1

@OptIn(ExperimentalCoroutinesApi::class)
class PaginationMediator @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val dispatchers: DispatcherProvider,
) {

    private val pageRequests = MutableSharedFlow<Int>(replay = 1)

    fun getPhotosFlow(): Flow<PaginationState> {
        return pageRequests.distinctUntilChanged().flatMapConcat { requestedPage ->
            flow {
                val local = photoRepository.getLocalPhotos(requestedPage)
                if (local.isNotEmpty() && !photoRepository.isCacheStale(requestedPage)) {
                    emit(PaginationState(photos = local, hasMore = true, nextPage = requestedPage + 1))
                } else {
                    val remote = photoRepository.getRemotePhotos(requestedPage)
                    if (remote.isEmpty()) {
                        emit(PaginationState(photos = emptyList(), hasMore = false, nextPage = requestedPage))
                    } else {
                        photoRepository.savePhotos(requestedPage, remote)
                        val saved = photoRepository.getLocalPhotos(requestedPage)
                        emit(PaginationState(photos = saved, hasMore = true, nextPage = requestedPage + 1))
                    }
                }
            }.flowOn(dispatchers.io)
        }
    }

    fun requestPage(page: Int) {
        pageRequests.tryEmit(page)
    }

    suspend fun refresh() {
        photoRepository.clearPhotos()
        pageRequests.emit(FIRST_PAGE)
    }

    data class PaginationState(
        val photos: List<Photo>,
        val hasMore: Boolean,
        val nextPage: Int,
    )
}
