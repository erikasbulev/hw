package com.z.photos.data.persistence.di

import android.content.Context
import androidx.room.Room
import com.z.photos.data.persistence.AppDatabase
import com.z.photos.data.persistence.room.dao.FavoritePhotoDao
import com.z.photos.data.persistence.room.dao.LocalPhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "photos-database"

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext applicationContext: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }

    @Provides
    fun provideLocalPhotoDao(appDatabase: AppDatabase): LocalPhotoDao = appDatabase.localPhotoDao()

    @Provides
    fun provideFavoritePhotoDao(appDatabase: AppDatabase): FavoritePhotoDao = appDatabase.favoritePhotoDao()
}
