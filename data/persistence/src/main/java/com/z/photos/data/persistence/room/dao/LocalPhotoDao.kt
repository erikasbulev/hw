package com.z.photos.data.persistence.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.z.photos.data.persistence.room.entities.LocalPhoto
import com.z.photos.data.persistence.room.entities.LocalPhotoWithFavorite

private const val SELECT_WITH_FAVORITE = """
    SELECT localphoto.id, localphoto.photoUrl, localphoto.photoThumbnailUrl, localphoto.artist,
           localphoto.page, localphoto.cachedAt,
           CASE WHEN favoritephoto.id IS NOT NULL THEN 1 ELSE 0 END AS isFavorite
    FROM localphoto
    LEFT JOIN favoritephoto ON localphoto.id = favoritephoto.id
"""

private const val SELECT_FAVORITES_ONLY = """
    SELECT localphoto.id, localphoto.photoUrl, localphoto.photoThumbnailUrl, localphoto.artist,
           localphoto.page, localphoto.cachedAt,
           1 AS isFavorite
    FROM localphoto
    INNER JOIN favoritephoto ON localphoto.id = favoritephoto.id
"""

@Dao
interface LocalPhotoDao {

    @Query("$SELECT_WITH_FAVORITE WHERE localphoto.page = :page")
    fun getPhotos(page: Int): List<LocalPhotoWithFavorite>

    @Query("$SELECT_WITH_FAVORITE WHERE localphoto.id = :id LIMIT 1")
    fun getPhotoById(id: Long): LocalPhotoWithFavorite?

    @Query(SELECT_FAVORITES_ONLY)
    fun getFavoritePhotos(): List<LocalPhotoWithFavorite>

    @Insert(onConflict = REPLACE)
    fun insertPhotos(photos: List<LocalPhoto>)

    @Query("DELETE FROM localphoto")
    fun clearPhotos()
}
