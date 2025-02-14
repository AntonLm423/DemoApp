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

        const val BASE_URL = ""

        private const val COOKIE_SHARED_PREFERENCES = "COOKIE_SHARED_PREFERENCES"

        const val API_PATH = "api"

        const val CAPTCHA_PATH = "xpvnsulc"

        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_X_EXCHANGE_SETUP_ID = "X-Exchange-Setup-Id"
        const val HEADER_X_APP_VERSION = "X-App-Version"
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_X_ACCESS_TOKEN = "X-Access-Token"
        const val HEADER_X_REGION_ID = "X-Region-Id"
        const val HEADER_X_AUTH_TOKEN = "X-Auth-Token"
        const val HEADER_X_CART_TOKEN = "X-Cart-Token"
        const val HEADER_ACCEPT_VERSION = "Accept-Version"
        const val HEADER_X_CANARY_VERSION = "X-Canary-Version"

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
        app: App,
        preferenceStorage: PreferenceStorage,
        context: Context,
        gson: Gson
    ): OkHttpClient {
        val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
            )
            .build()

        return OkHttpClient.Builder().apply {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                connectionSpecs(listOf(spec))
            }

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