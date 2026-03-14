package com.z.photos.business.repositories

import com.z.photos.business.entities.Photo

interface PhotoRepository {
    suspend fun getPhotos(page: Int): List<Photo>
}