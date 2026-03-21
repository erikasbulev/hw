package com.z.photos.business.repositories

import com.z.photos.business.entities.Photo

interface PhotoRepository {

    suspend fun getRemotePhotos(page: Int): List<Photo>

    suspend fun getLocalPhotos(page: Int): List<Photo>

    suspend fun getLocalPhoto(id: Long): Photo?

    suspend fun savePhotos(page: Int, photos: List<Photo>)

    suspend fun favoritePhoto(photoId: Long)

    suspend fun unfavoritePhoto(photoId: Long)
}
