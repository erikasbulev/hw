package com.z.photos.nework.`interface`

import com.z.photos.nework.response.CuratedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixelsApi {

    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): CuratedResponse
}