package com.z.photos.business.datasources

import com.z.photos.business.entities.Photo

interface RemoteDataSource {

    suspend fun getPhotos(page: Int): List<Photo>
}