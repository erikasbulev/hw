package com.z.photos.nework.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class PexelsAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "apiKey")
            .build()
        return chain.proceed(request)
    }
}