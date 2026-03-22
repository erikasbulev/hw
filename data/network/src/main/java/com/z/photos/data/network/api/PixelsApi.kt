package com.z.photos.data.network.api

import com.z.photos.data.network.entities.PhotoEntity
import com.z.photos.data.network.response.CuratedResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PixelsApi {

    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null,
    ): CuratedResponse

    @GET("v1/photos/{id}")
    suspend fun getPhoto(@Path("id") id: Long): PhotoEntity
}
