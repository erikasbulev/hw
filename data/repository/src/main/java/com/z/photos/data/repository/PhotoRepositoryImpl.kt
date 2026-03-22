package com.z.photos.data.repository

import com.z.photos.data.network.datasource.RemoteDataSource
import com.z.photos.data.persistence.datasource.LocalDataSource
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
import com.z.photos.domain.time.TimeProvider
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
    private val timeProvider: TimeProvider,
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

    override suspend fun isCacheStale(page: Int): Boolean {
        val cachedAt = localDataSource.getCacheTimestamp(page) ?: return true
        return timeProvider.currentTimeMillis() - cachedAt > CACHE_TTL_MS
    }

    companion object {
        private const val CACHE_TTL_MS = 5 * 60 * 1000L
    }
}
