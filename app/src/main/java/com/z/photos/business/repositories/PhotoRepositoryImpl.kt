package com.z.photos.business.repositories

import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.entities.Photo

class PhotoRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : PhotoRepository {

    override suspend fun getPhotos(page: Int): List<Photo> {
        return localDataSource.getPhotos(page) ?: remoteDataSource.getPhotos(page)
    }
}