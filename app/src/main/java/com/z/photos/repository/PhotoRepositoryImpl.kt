package com.z.photos.repository

import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : PhotoRepository {

    override suspend fun getRemotePhotos(page: Int): List<Photo> {
        return remoteDataSource.getPhotos(page)
    }

    override suspend fun getLocalPhotos(page: Int): List<Photo> {
        return localDataSource.getPhotos(page)
    }

    override suspend fun savePhotos(page: Int, photos: List<Photo>) {
        localDataSource.persistPhotos(page = page, remotePhotos = photos)
    }
}