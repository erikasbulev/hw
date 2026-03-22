package com.z.photos.domain.repositories

import com.z.photos.domain.entities.Photo

interface PhotoRepository {

    suspend fun getRemotePhotos(page: Int): List<Photo>

    suspend fun getLocalPhotos(page: Int): List<Photo>

    suspend fun getPhoto(id: Long): Photo

    suspend fun savePhotos(page: Int, photos: List<Photo>)

    suspend fun isCacheStale(page: Int): Boolean

    suspend fun clearPhotos()
}
