package com.z.photos.di

import com.z.photos.data.network.RemoteApiDataSource
import com.z.photos.data.persistence.RoomDataSource
import com.z.photos.data.repository.PhotoRepositoryImpl
import com.z.photos.data.network.datasource.RemoteDataSource
import com.z.photos.data.persistence.datasource.LocalDataSource
import com.z.photos.data.persistence.time.TimeProvider
import com.z.photos.domain.repositories.PhotoRepository
import com.z.photos.time.SystemTimeProvider
import com.z.photos.ui.core.DefaultDispatcherProvider
import com.z.photos.ui.core.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TestModuleForNow {

    @Binds
    abstract fun bindPhotoRepository(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    abstract fun bindRemoteDataSource(impl: RemoteApiDataSource): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(impl: RoomDataSource): LocalDataSource

    @Binds
    abstract fun bindTimeProvider(impl: SystemTimeProvider): TimeProvider

    @Binds
    abstract fun bindDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider
}
