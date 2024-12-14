package com.ob.rfi.api

import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException


class ErrorInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (e: ConnectException) {
            // Handle ConnectException here
            throw IOException(
                "Unable to connect to the server. Please check your internet connection.",
                e
            )
        } catch (e: Exception) {
            // Handle other exceptions
            throw IOException("An unexpected error occurred.", e)
        }
    }
}