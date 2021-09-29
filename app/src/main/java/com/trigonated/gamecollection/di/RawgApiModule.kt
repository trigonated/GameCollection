package com.trigonated.gamecollection.di

import com.trigonated.gamecollection.api.rawg.RawgService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RawgApiModule {

    @Singleton
    @Provides
    fun provideRawgService(): RawgService {
        return RawgService.create()
    }
}