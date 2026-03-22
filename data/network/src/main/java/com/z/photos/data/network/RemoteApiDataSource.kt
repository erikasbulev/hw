package com.z.photos.data.network

import com.z.photos.data.network.api.PixelsApi
import com.z.photos.data.network.response.CuratedResponse
import com.z.photos.data.network.datasource.RemoteDataSource
import com.z.photos.domain.entities.Photo
import javax.inject.Inject

private const val PER_PAGE = 20

class RemoteApiDataSource @Inject constructor(
    private val api: PixelsApi,
) : RemoteDataSource {

    override suspend fun getPhotos(page: Int): List<Photo> {
        return api.getCuratedPhotos(
            perPage = PER_PAGE,
            page = page
        ).toPhotos()
    }

    private fun CuratedResponse.toPhotos(): List<Photo> {
        return this.photos.map {
            Photo(
                id = it.id,
                photoUrl = it.photoSource.original,
                isFavorite = false,
                photoThumbnailUrl = it.photoSource.tiny,
                artist = it.photographer,
            )
        }
    }
}
