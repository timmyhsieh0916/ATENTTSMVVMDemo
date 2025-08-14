package com.timmy.networklibs.di

import android.app.Application
import android.content.Context
import com.timmy.networklibs.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    @Named("BackendApi")
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(
                    if (BuildConfig.DEBUG) {
                        // Debug 模式下才開啟
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            })
            .build()
    }

    @Singleton
    @Provides
    @Named("TtsApi")
    fun provideTTsOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(
                    if (BuildConfig.DEBUG) {
                        // Debug 模式下才開啟
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            })
            .addInterceptor(AuthInterceptor { "aa1be94fb419426da458cf5e3e2013e9" })
            .build()
    }

    @Provides
    @Named("TtsApi")
    fun provideRetrofit(@Named("TtsApi") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.aivoice.com.tw/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class AuthInterceptor(private val tokenProvider: () -> String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = tokenProvider()
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", token)
                .build()
            return chain.proceed(newRequest)
        }
    }

}