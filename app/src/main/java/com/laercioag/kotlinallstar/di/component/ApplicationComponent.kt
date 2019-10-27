package com.laercioag.kotlinallstar.di.component

import com.laercioag.kotlinallstar.di.module.AndroidModule
import com.laercioag.kotlinallstar.di.module.AppModule
import com.laercioag.kotlinallstar.di.module.DataModule
import com.laercioag.kotlinallstar.di.module.ViewModelModule
import com.laercioag.kotlinallstar.ui.base.BaseApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        AndroidModule::class,
        ViewModelModule::class,
        DataModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<BaseApplication>

}