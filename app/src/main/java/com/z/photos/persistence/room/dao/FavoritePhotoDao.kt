package com.z.photos.persistence.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.z.photos.persistence.room.entities.FavoritePhoto

@Dao
interface FavoritePhotoDao {

    @Insert(onConflict = REPLACE)
    fun insertFavorite(photo: FavoritePhoto)

    @Delete
    fun deleteFavorite(photo: FavoritePhoto)
}
