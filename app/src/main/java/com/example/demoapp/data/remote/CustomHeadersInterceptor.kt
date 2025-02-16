package com.example.demoapp.data.remote

import com.example.demoapp.di.module.AppModule
import okhttp3.Interceptor
import okhttp3.Response

class CustomHeadersInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header(AppModule.HEADER_ACCEPT, AppModule.CONTENT_TYPE)

        val request = requestBuilder.method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }

}