package com.z.photos.persistence.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.z.photos.persistence.room.entities.LocalPhoto

@Dao
interface LocalPhotoDao {

    @Query("SELECT * FROM localphoto WHERE page = :page")
    fun getPhotos(page: Int): List<LocalPhoto>

    @Query("SELECT * FROM localphoto WHERE id = :id LIMIT 1")
    fun getPhotoById(id: Long): LocalPhoto?

    @Insert(onConflict = REPLACE)
    fun insertPhotos(remotePhotos: List<LocalPhoto>)
}