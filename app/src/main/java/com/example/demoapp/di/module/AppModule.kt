package com.example.demoapp.di.module

import android.app.Application
import android.content.Context
import android.os.Build
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.demoapp.data.local.prefs.PreferenceHelper.defaultPrefs
import com.example.demoapp.data.local.prefs.PreferenceStorage
import com.example.demoapp.data.remote.ApiService
import com.example.demoapp.di.App
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class AppModule {

    companion object {
        const val CONNECTION_TIMEOUT = 60000L

        const val API_VERSION = "2.60"

        const val BASE_URL = "https://api.kinopoisk.dev/"
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
        gson: Gson,
        client: OkHttpClient,
    ): ApiService {
        return Retrofit.Builder()
            .client(client)
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
            addInterceptor(ChuckerInterceptor(app))
        }.build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }
}