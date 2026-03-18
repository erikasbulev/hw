package com.z.photos.di

import com.z.photos.nework.api.PixelsApi
import com.z.photos.nework.interceptors.PexelsAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class NetworkModule {

    @Provides
    fun providesPixelsApi(httpClient: OkHttpClient): PixelsApi {
        return Retrofit.Builder()
            .baseUrl("https://api.pexels.com/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PixelsApi::class.java)
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(PexelsAuthInterceptor())
            .build()
    }
}