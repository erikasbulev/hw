package com.z.photos.domain.repositories

import com.z.photos.domain.entities.Photo

interface FavoritesRepository {

    suspend fun favoritePhoto(photoId: Long)

    suspend fun unfavoritePhoto(photoId: Long)

    suspend fun getFavoritePhotos(): List<Photo>

    suspend fun getFavoriteCount(): Int
}
