package com.z.photos.persistence.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.z.photos.business.entities.Photo

@Dao
interface LocalPhotoDao {

    @Query("SELECT * FROM localphoto")
    fun getPhotos(): List<Photo>
}