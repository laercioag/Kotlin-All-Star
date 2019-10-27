package com.laercioag.kotlinallstar.di.module

import com.laercioag.kotlinallstar.ui.list.ListFragment
import com.laercioag.kotlinallstar.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindListFragment(): ListFragment
}