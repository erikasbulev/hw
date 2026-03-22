package com.z.photos.domain.repositories

import com.z.photos.domain.entities.Photo

interface PhotoRepository {

    suspend fun getRemotePhotos(page: Int): List<Photo>

    suspend fun getLocalPhotos(page: Int): List<Photo>

    suspend fun getLocalPhoto(id: Long): Photo?

    suspend fun savePhotos(page: Int, photos: List<Photo>)

    suspend fun favoritePhoto(photoId: Long)

    suspend fun unfavoritePhoto(photoId: Long)

    suspend fun getFavoritePhotos(): List<Photo>

    suspend fun getFavoriteCount(): Int

    suspend fun isCacheStale(page: Int): Boolean
}
