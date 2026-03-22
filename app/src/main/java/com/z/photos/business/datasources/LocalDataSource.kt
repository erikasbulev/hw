package com.z.photos.business.datasources

import com.z.photos.business.entities.Photo

interface LocalDataSource {

    suspend fun getPhotos(page: Int): List<Photo>

    suspend fun getPhoto(id: Long): Photo?

    suspend fun persistPhotos(page: Int, remotePhotos: List<Photo>)

    suspend fun favorite(id: Long)

    suspend fun unfavorite(id: Long)

    suspend fun getFavoritePhotos(): List<Photo>

    suspend fun getFavoriteCount(): Int
}
