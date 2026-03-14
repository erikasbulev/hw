package com.z.photos.nework

import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.entities.Photo

class RemoteApiDataSource : RemoteDataSource {

    override suspend fun getPhotos(page: Int): List<Photo> {
        TODO("Not yet implemented")
    }
}