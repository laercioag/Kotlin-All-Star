package com.laercioag.kotlinallstar.di.component

import com.laercioag.kotlinallstar.di.module.AndroidModule
import com.laercioag.kotlinallstar.ui.base.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AndroidModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: DaggerApplication): Builder

        fun build(): ApplicationComponent
    }

    override fun inject(application: BaseApplication)
}