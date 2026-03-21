package com.z.photos.nework

import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.entities.Photo
import com.z.photos.nework.api.PixelsApi
import com.z.photos.nework.response.CuratedResponse
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
                isFavorite = false, // todo maybe favorite from db?
                photoThumbnailUrl = it.photoSource.tiny,
                artist = it.photographer
            )
        }
    }
}