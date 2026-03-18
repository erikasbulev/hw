package com.z.photos.persistence

import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.entities.Photo
import com.z.photos.persistence.room.dao.LocalPhotoDao
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val localPhotoDao: LocalPhotoDao,
) : LocalDataSource {

    override suspend fun getPhotos(): List<Photo>? {
        return emptyList()
    }
}