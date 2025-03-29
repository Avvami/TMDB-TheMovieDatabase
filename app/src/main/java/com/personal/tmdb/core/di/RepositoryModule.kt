package com.personal.tmdb.core.di

import com.personal.tmdb.core.data.repository.DominantColorRepositoryImpl
import com.personal.tmdb.core.data.repository.LocalCacheImpl
import com.personal.tmdb.core.data.repository.PreferencesRepositoryImpl
import com.personal.tmdb.core.data.repository.UserRepositoryImpl
import com.personal.tmdb.core.domain.repository.DominantColorRepository
import com.personal.tmdb.core.domain.repository.LocalCache
import com.personal.tmdb.core.domain.repository.PreferencesRepository
import com.personal.tmdb.core.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindLocalCache(
        localCacheImpl: LocalCacheImpl
    ): LocalCache

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindDominantColorRepository(
        dominantColorRepositoryImpl: DominantColorRepositoryImpl
    ): DominantColorRepository
}