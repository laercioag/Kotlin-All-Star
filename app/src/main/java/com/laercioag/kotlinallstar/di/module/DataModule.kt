package com.laercioag.kotlinallstar.di.module

import android.content.Context
import androidx.room.Room
import com.laercioag.kotlinallstar.data.local.database.AppDatabase
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapper
import com.laercioag.kotlinallstar.data.mapper.RepositoryMapperImpl
import com.laercioag.kotlinallstar.data.remote.api.Api
import com.laercioag.kotlinallstar.data.remote.api.ApiImpl
import com.laercioag.kotlinallstar.data.remote.service.RemoteService
import com.laercioag.kotlinallstar.data.repository.RepositoriesRepository
import com.laercioag.kotlinallstar.data.repository.RepositoriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [DataModule.Binder::class])
class DataModule {

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRemoteService(retrofit: Retrofit): RemoteService =
        retrofit.create(RemoteService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@AppContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "kotlin-all-star-db"
        ).build()

    @Module
    abstract class Binder {
        @Binds
        abstract fun bindApi(impl: ApiImpl): Api

        @Binds
        abstract fun bindRepositoryMapper(impl: RepositoryMapperImpl): RepositoryMapper

        @Binds
        abstract fun bindRepositoriesRepository(impl: RepositoriesRepositoryImpl): RepositoriesRepository
    }

}