package com.z.photos.data.repository

import com.z.photos.data.network.datasource.RemoteDataSource
import com.z.photos.data.persistence.datasource.LocalDataSource
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.FavoritesRepository
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val favoriteChangeNotifier: FavoriteChangeNotifier,
) : FavoritesRepository {

    override suspend fun favoritePhoto(photoId: Long) {
        localDataSource.favorite(photoId)
        favoriteChangeNotifier.notifyChange()
    }

    override suspend fun unfavoritePhoto(photoId: Long) {
        localDataSource.unfavorite(photoId)
        favoriteChangeNotifier.notifyChange()
    }

    override suspend fun getFavoritePhotos(): List<Photo> {
        val localFavorites = localDataSource.getFavoritePhotos()
        if (localFavorites.isNotEmpty()) return localFavorites

        val favoriteIds = localDataSource.getFavoriteIds()
        return favoriteIds.map { id ->
            remoteDataSource.getPhoto(id).copy(isFavorite = true)
        }
    }

    override suspend fun getFavoriteCount(): Int {
        return localDataSource.getFavoriteCount()
    }
}
