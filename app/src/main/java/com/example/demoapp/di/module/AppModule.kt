package com.example.demoapp.di.module

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.demoapp.data.local.prefs.PreferenceHelper.defaultPrefs
import com.example.demoapp.data.local.prefs.PreferenceStorage
import com.example.demoapp.data.remote.ApiService
import com.example.demoapp.data.remote.AuthInterceptor
import com.example.demoapp.data.remote.CustomHeadersInterceptor
import com.example.demoapp.di.App
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class AppModule {

    companion object {
        const val CONNECTION_TIMEOUT = 60000L

        const val BASE_URL = "https://api.kinopoisk.dev/"

        const val HEADER_X_API_KEY = "X-API-KEY"
        const val HEADER_ACCEPT = "accept"

        const val CONTENT_TYPE = "application/json"
    }

    @Provides
    fun provideContext(app: App): Context {
        return app.applicationContext
    }

    @Provides
    fun provideApplication(app: App): Application {
        return app
    }

    @Provides
    @Singleton
    fun providePreferenceStorage(context: Context, gson: Gson): PreferenceStorage {
        return PreferenceStorage(context, defaultPrefs(context), gson)
    }

    @Provides
    @Singleton
    fun provideApiService(
        client: OkHttpClient,
        gson: Gson
    ): ApiService {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        app: App
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            addInterceptor(AuthInterceptor())
            addInterceptor(CustomHeadersInterceptor())
            addInterceptor(ChuckerInterceptor(app))
        }.build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }
}
