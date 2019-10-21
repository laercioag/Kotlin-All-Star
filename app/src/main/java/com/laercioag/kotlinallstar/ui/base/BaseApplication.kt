package com.laercioag.kotlinallstar.ui.base

import com.laercioag.kotlinallstar.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class BaseApplication : DaggerApplication() {

    private val applicationInjector = DaggerApplicationComponent
        .factory()
        .create(this)

    override fun applicationInjector(): AndroidInjector<BaseApplication> = applicationInjector
}
