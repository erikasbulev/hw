package com.z.photos.ui.feed.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import jakarta.inject.Inject

class FeedPagingSource @Inject constructor(
    private val photoRepository: PhotoRepository,
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1

        return try {
            val photos = photoRepository.getPhotos(
                page = page
            )

            LoadResult.Page(
                data = photos,
                prevKey = null,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int = 1
}