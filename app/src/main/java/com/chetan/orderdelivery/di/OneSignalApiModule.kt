package com.chetan.orderdelivery.di

import com.chetan.orderdelivery.data.repositoryImpl.OneSignalRepositoryImpl
import com.chetan.orderdelivery.domain.repository.OneSignalRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object OneSignalApiModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient{
        val interceptors = HttpLoggingInterceptor()
        interceptors.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptors)
            .build()
    }
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://onesignal.com/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideOneSignalApiService(retrofit: Retrofit) : OneSignalRepository{
        return retrofit.create(OneSignalRepository::class.java)
    }
    @Provides
    fun provideOneSignalRepository(repository: OneSignalRepository) : OneSignalRepositoryImpl{
        return OneSignalRepositoryImpl(repository)
    }
}