package com.example.demoapp.data.remote

import com.example.demoapp.di.module.AppModule
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header(AppModule.HEADER_X_API_KEY, "EXMYHJB-VNH4J3H-NTRY7DJ-PG78H90")
        // TODO: hide api key

        val request = requestBuilder.method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }
}
