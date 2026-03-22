package com.z.photos.data.network.di

import com.z.photos.data.network.api.PixelsApi
import com.z.photos.data.network.interceptors.PexelsAuthInterceptor
import com.z.photos.data.network.interceptors.RetryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
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
            .addInterceptor(RetryInterceptor())
            .build()
    }
}
