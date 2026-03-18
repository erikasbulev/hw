package com.z.photos.business.datasources

import com.z.photos.business.entities.Photo

interface LocalDataSource {

    suspend fun getPhotos(): List<Photo>?
}