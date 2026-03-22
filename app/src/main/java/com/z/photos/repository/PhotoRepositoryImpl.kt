package com.z.photos.repository

import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.entities.Photo
import com.z.photos.business.repositories.PhotoRepository
import com.z.photos.ui.FavoriteChangeNotifier
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
) : PhotoRepository {

    override suspend fun getRemotePhotos(page: Int): List<Photo> {
        return remoteDataSource.getPhotos(page)
    }

    override suspend fun getLocalPhotos(page: Int): List<Photo> {
        return localDataSource.getPhotos(page)
    }

    override suspend fun getLocalPhoto(id: Long): Photo? {
        return localDataSource.getPhoto(id)
    }

    override suspend fun savePhotos(page: Int, photos: List<Photo>) {
        localDataSource.persistPhotos(page, photos)
    }

    override suspend fun favoritePhoto(photoId: Long) {
        localDataSource.favorite(photoId)
        favoriteChangeNotifier.notifyChange()
    }

    override suspend fun unfavoritePhoto(photoId: Long) {
        localDataSource.unfavorite(photoId)
        favoriteChangeNotifier.notifyChange()
    }

    override suspend fun getFavoritePhotos(): List<Photo> {
        return localDataSource.getFavoritePhotos()
    }

    override suspend fun getFavoriteCount(): Int {
        return localDataSource.getFavoriteCount()
    }
}
