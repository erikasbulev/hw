package com.z.photos.persistence

import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.entities.Photo
import com.z.photos.persistence.room.dao.LocalPhotoDao
import com.z.photos.persistence.room.entities.LocalPhoto
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val localPhotoDao: LocalPhotoDao,
) : LocalDataSource {

    override suspend fun getPhotos(page: Int): List<Photo> {
        return localPhotoDao.getPhotos(page).map { it.toPhoto() }
    }

    override suspend fun persistPhotos(page: Int, remotePhotos: List<Photo>) {
        localPhotoDao.insertPhotos(remotePhotos.map { it.toLocalPhoto(page) })
    }

    private fun LocalPhoto.toPhoto(): Photo {
        return Photo(
            id = this.id,
            photoUrl = this.photoUrl,
            isFavorite = this.isFavorite,
            photoThumbnailUrl = this.photoThumbnailUrl,
            artist = this.artist,
        )
    }

    private fun Photo.toLocalPhoto(page: Int): LocalPhoto {
        return LocalPhoto(
            id = this.id,
            photoUrl = this.photoUrl,
            isFavorite = this.isFavorite,
            photoThumbnailUrl = this.photoThumbnailUrl,
            artist = this.artist,
            page = page,
        )
    }
}