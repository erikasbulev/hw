package com.z.photos.data.persistence

import com.z.photos.data.persistence.room.dao.FavoritePhotoDao
import com.z.photos.data.persistence.room.dao.LocalPhotoDao
import com.z.photos.data.persistence.room.entities.FavoritePhoto
import com.z.photos.data.persistence.room.entities.LocalPhoto
import com.z.photos.data.persistence.room.entities.LocalPhotoWithFavorite
import com.z.photos.data.persistence.datasource.LocalDataSource
import com.z.photos.domain.entities.Photo
import com.z.photos.data.persistence.time.TimeProvider
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val localPhotoDao: LocalPhotoDao,
    private val favoritePhotoDao: FavoritePhotoDao,
    private val timeProvider: TimeProvider,
) : LocalDataSource {

    override suspend fun getPhotos(page: Int): List<Photo> {
        return localPhotoDao.getPhotos(page).map { it.toPhoto() }
    }

    override suspend fun getPhoto(id: Long): Photo? {
        return localPhotoDao.getPhotoById(id)?.toPhoto()
    }

    override suspend fun persistPhotos(page: Int, remotePhotos: List<Photo>) {
        val now = timeProvider.currentTimeMillis()
        localPhotoDao.insertPhotos(remotePhotos.map { it.toLocalPhoto(page, now) })
    }

    override suspend fun favorite(id: Long) {
        favoritePhotoDao.insertFavorite(FavoritePhoto(id))
    }

    override suspend fun unfavorite(id: Long) {
        favoritePhotoDao.deleteFavorite(FavoritePhoto(id))
    }

    override suspend fun getFavoritePhotos(): List<Photo> {
        return localPhotoDao.getFavoritePhotos().map { it.toPhoto() }
    }

    override suspend fun getFavoriteCount(): Int {
        return favoritePhotoDao.getFavoriteCount()
    }

    private fun LocalPhotoWithFavorite.toPhoto(): Photo {
        return Photo(
            id = id,
            photoUrl = photoUrl,
            isFavorite = isFavorite,
            photoThumbnailUrl = photoThumbnailUrl,
            artist = artist,
        )
    }

    override suspend fun clearPhotos() {
        localPhotoDao.clearPhotos()
    }

    override suspend fun isFavorite(id: Long): Boolean {
        return favoritePhotoDao.isFavorite(id)
    }

    override suspend fun getFavoriteIds(): List<Long> {
        return favoritePhotoDao.getFavoriteIds()
    }

    override suspend fun getCacheTimestamp(page: Int): Long? {
        return localPhotoDao.getPhotos(page).firstOrNull()?.cachedAt
    }

    private fun Photo.toLocalPhoto(page: Int, cachedAt: Long): LocalPhoto {
        return LocalPhoto(
            id = id,
            photoUrl = photoUrl,
            photoThumbnailUrl = photoThumbnailUrl,
            artist = artist,
            page = page,
            cachedAt = cachedAt,
        )
    }
}
