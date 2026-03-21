package com.z.photos.persistence

import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.entities.Photo
import com.z.photos.persistence.room.dao.FavoritePhotoDao
import com.z.photos.persistence.room.dao.LocalPhotoDao
import com.z.photos.persistence.room.entities.FavoritePhoto
import com.z.photos.persistence.room.entities.LocalPhoto
import com.z.photos.persistence.room.entities.LocalPhotoWithFavorite
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val localPhotoDao: LocalPhotoDao,
    private val favoritePhotoDao: FavoritePhotoDao,
) : LocalDataSource {

    override suspend fun getPhotos(page: Int): List<Photo> {
        return localPhotoDao.getPhotos(page).map { it.toPhoto() }
    }

    override suspend fun getPhoto(id: Long): Photo? {
        return localPhotoDao.getPhotoById(id)?.toPhoto()
    }

    override suspend fun persistPhotos(page: Int, remotePhotos: List<Photo>) {
        localPhotoDao.insertPhotos(remotePhotos.map { it.toLocalPhoto(page) })
    }

    override suspend fun favorite(id: Long) {
        favoritePhotoDao.insertFavorite(FavoritePhoto(id))
    }

    override suspend fun unfavorite(id: Long) {
        favoritePhotoDao.deleteFavorite(FavoritePhoto(id))
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

    private fun Photo.toLocalPhoto(page: Int): LocalPhoto {
        return LocalPhoto(
            id = id,
            photoUrl = photoUrl,
            photoThumbnailUrl = photoThumbnailUrl,
            artist = artist,
            page = page,
        )
    }
}
