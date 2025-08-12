package com.example.data.DataSource.localeDataSource.LanServeis

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LanWebSocketProvider  {

    @Provides
    @Singleton
    fun provideGamePlayWebSocketFactory() :GamePlayLanWebSocketFactory{
        return GamePlayWepSocketFactoryImpl()
    }
}