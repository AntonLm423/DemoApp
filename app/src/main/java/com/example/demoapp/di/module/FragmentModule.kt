package com.example.demoapp.di.module

import com.example.demoapp.ui.catalog.CatalogFragment
import com.example.demoapp.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment

@Module(includes = [ViewModelModule::class])
abstract class FragmentModule {

    @ContributesAndroidInjector
    @PerFragment
    abstract fun mainFragment(): MainFragment

    @ContributesAndroidInjector
    @PerFragment
    abstract fun catalogFragment(): CatalogFragment
}
