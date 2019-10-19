package com.laercioag.kotlinallstar.ui.base

import com.laercioag.kotlinallstar.di.component.DaggerApplicationComponent
import dagger.android.support.DaggerApplication

class BaseApplication : DaggerApplication() {

    private val applicationInjector = DaggerApplicationComponent
        .builder()
        .application(this)
        .build()

    override fun applicationInjector() = applicationInjector
}
