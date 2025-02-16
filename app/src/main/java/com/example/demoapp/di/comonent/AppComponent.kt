package com.example.demoapp.di.comonent

import com.example.demoapp.di.App
import com.example.demoapp.di.module.ActivityModule
import com.example.demoapp.di.module.AppModule
import com.example.demoapp.di.module.FragmentModule
import com.example.demoapp.di.module.RepositoryModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
