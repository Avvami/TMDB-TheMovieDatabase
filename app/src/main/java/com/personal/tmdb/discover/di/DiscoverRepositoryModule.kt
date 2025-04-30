package com.personal.tmdb.discover.di

import com.personal.tmdb.discover.data.repository.DiscoverRepositoryImpl
import com.personal.tmdb.discover.domain.repository.DiscoverRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiscoverRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDiscoverRepository(
        discoverRepositoryImpl: DiscoverRepositoryImpl
    ): DiscoverRepository
}