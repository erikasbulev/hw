package com.z.photos.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.z.photos.data.persistence.room.dao.FavoritePhotoDao
import com.z.photos.data.persistence.room.dao.LocalPhotoDao
import com.z.photos.data.persistence.room.entities.FavoritePhoto
import com.z.photos.data.persistence.room.entities.LocalPhoto

@Database(
    entities = [LocalPhoto::class, FavoritePhoto::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun localPhotoDao(): LocalPhotoDao

    abstract fun favoritePhotoDao(): FavoritePhotoDao
}
