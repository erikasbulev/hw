package com.z.photos.repository

import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : PhotoRepository {

    override suspend fun getPhotos(page: Int): List<Photo> {
        return remoteDataSource.getPhotos(page)
    }
}