package com.z.photos.di

import android.content.Context
import androidx.room.Room
import com.z.photos.persistence.AppDatabase
import com.z.photos.persistence.room.dao.LocalPhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
class PersistenceModule {

    @Provides
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-name"
        ).build()
    }

    @Provides
    fun provideLocalPhotoDao(appDatabase: AppDatabase): LocalPhotoDao = appDatabase.localPhotoDao()
}