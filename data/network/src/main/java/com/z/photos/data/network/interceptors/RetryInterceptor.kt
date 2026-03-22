package com.z.photos.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val MAX_RETRIES = 3

class RetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var lastException: IOException? = null

        repeat(MAX_RETRIES) { attempt ->
            try {
                val response = chain.proceed(request)
                if (response.isSuccessful || isClientError(response.code)) {
                    return response
                }
                response.close()
            } catch (e: IOException) {
                lastException = e
            }
        }

        throw lastException ?: IOException("Request failed after $MAX_RETRIES retries")
    }

    private fun isClientError(code: Int): Boolean = code in 400..499
}
