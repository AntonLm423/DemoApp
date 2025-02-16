package com.example.demoapp.di.module

import com.example.demoapp.data.repository.CatalogRepository
import com.example.demoapp.data.repository.impl.CatalogRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun catalogRepository(repository: CatalogRepositoryImpl): CatalogRepository
}
