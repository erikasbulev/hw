package com.z.photos.data.persistence.datasource

import com.z.photos.domain.entities.Photo

interface LocalDataSource {

    suspend fun getPhotos(page: Int): List<Photo>

    suspend fun getPhoto(id: Long): Photo?

    suspend fun persistPhotos(page: Int, remotePhotos: List<Photo>)

    suspend fun favorite(id: Long)

    suspend fun unfavorite(id: Long)

    suspend fun getFavoritePhotos(): List<Photo>

    suspend fun getFavoriteCount(): Int

    suspend fun getCacheTimestamp(page: Int): Long?

    suspend fun isFavorite(id: Long): Boolean

    suspend fun getFavoriteIds(): List<Long>
}
