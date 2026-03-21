package com.z.photos.persistence.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.z.photos.persistence.room.entities.LocalPhoto
import com.z.photos.persistence.room.entities.LocalPhotoWithFavorite

private const val SELECT_WITH_FAVORITE = """
    SELECT localphoto.id, localphoto.photoUrl, localphoto.photoThumbnailUrl, localphoto.artist, localphoto.page,
           CASE WHEN favoritephoto.id IS NOT NULL THEN 1 ELSE 0 END AS isFavorite
    FROM localphoto
    LEFT JOIN favoritephoto ON localphoto.id = favoritephoto.id
"""

@Dao
interface LocalPhotoDao {

    @Query("$SELECT_WITH_FAVORITE WHERE localphoto.page = :page")
    fun getPhotos(page: Int): List<LocalPhotoWithFavorite>

    @Query("$SELECT_WITH_FAVORITE WHERE localphoto.id = :id LIMIT 1")
    fun getPhotoById(id: Long): LocalPhotoWithFavorite?

    @Insert(onConflict = REPLACE)
    fun insertPhotos(photos: List<LocalPhoto>)
}
