package com.z.photos.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class PexelsAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "7aTs5JVIG3QhBE811MLDkWGOuxdCY7Af3IQp9Fu537DormA5Y6XWei3c")
            .build()
        return chain.proceed(request)
    }
}
