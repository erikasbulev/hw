package com.z.photos.data.persistence.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.z.photos.data.persistence.room.entities.FavoritePhoto

@Dao
interface FavoritePhotoDao {

    @Insert(onConflict = REPLACE)
    fun insertFavorite(photo: FavoritePhoto)

    @Delete
    fun deleteFavorite(photo: FavoritePhoto)

    @Query("SELECT COUNT(*) FROM favoritephoto")
    fun getFavoriteCount(): Int
}
