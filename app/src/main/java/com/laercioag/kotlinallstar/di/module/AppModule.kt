package com.laercioag.kotlinallstar.di.module

import android.content.Context
import com.laercioag.kotlinallstar.ui.base.BaseApplication
import dagger.Binds
import dagger.Module
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppContext

@Module
abstract class AppModule {

    @Singleton
    @Binds
    @AppContext
    abstract fun provideContext(app: BaseApplication): Context
}