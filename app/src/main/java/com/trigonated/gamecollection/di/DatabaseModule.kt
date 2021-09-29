package com.trigonated.gamecollection.di

import android.content.Context
import com.trigonated.gamecollection.model.database.AppDatabase
import com.trigonated.gamecollection.model.database.dao.GameCollectionStatusDao
import com.trigonated.gamecollection.model.database.dao.GameDao
import com.trigonated.gamecollection.model.database.dao.GamePlatformDao
import com.trigonated.gamecollection.model.database.dao.PlatformDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideGameDao(appDatabase: AppDatabase): GameDao {
        return appDatabase.gameDao()
    }

    @Provides
    fun providePlatformDao(appDatabase: AppDatabase): PlatformDao {
        return appDatabase.platformDao()
    }

    @Provides
    fun provideGamePlatformDao(appDatabase: AppDatabase): GamePlatformDao {
        return appDatabase.gamePlatformDao()
    }

    @Provides
    fun provideGameCollectionStatusDao(appDatabase: AppDatabase): GameCollectionStatusDao {
        return appDatabase.gameCollectionStatusDao()
    }
}