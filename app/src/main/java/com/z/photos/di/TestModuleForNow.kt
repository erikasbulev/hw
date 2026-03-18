package com.z.photos.di

import android.app.Application
import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.repositories.PhotoRepository
import com.z.photos.nework.RemoteApiDataSource
import com.z.photos.repository.PhotoRepositoryImpl
import com.z.photos.persistence.RoomDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TestModuleForNow {

    @Binds
    abstract fun bindPhotoRepository(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    abstract fun bindRemoteDataSource(impl: RemoteApiDataSource): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(impl: RoomDataSource): LocalDataSource
}