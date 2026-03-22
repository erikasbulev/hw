package com.z.photos.data.network.datasource

import com.z.photos.domain.entities.Photo

interface RemoteDataSource {

    suspend fun getPhotos(page: Int): List<Photo>
}
