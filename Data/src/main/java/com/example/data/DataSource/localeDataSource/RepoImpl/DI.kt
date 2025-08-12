package com.example.data.DataSource.localeDataSource.RepoImpl

import android.content.Context
import com.example.data.DataSource.localeDataSource.LanServeis.GamePlayLanWebSocketFactory
import com.example.data.DataSource.localeDataSource.LanServeis.GameWebSocketClient
import com.example.domain.Repo.GamePlayRepo
import com.example.domain.Repo.LanGamePLay
import com.example.domain.Repo.SinglePlayerRepo
import com.example.domain.Utlites.getMyIpAddress
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.net.URI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamePlayProvider{
    @Provides
    @Singleton
    fun providesGamePlayRepo() : GamePlayRepo {
        return GamePlayRepoImpl()
    }
    @Provides
    @Singleton
    fun providesSinglePlayRepo() : SinglePlayerRepo {
        return SinglePlayRepoImpl()
    }


    @Singleton
    @Provides
    fun provideLanGameRepo( @ApplicationContext context: Context , gamePlayLanWebSocketFactory: GamePlayLanWebSocketFactory):LanGamePLay{
        return LanGamePlayRepoImpl( context = context , gamePlayLanWebSocketFactory = gamePlayLanWebSocketFactory )
    }


    @Singleton
    @Provides
    fun provideBaseUrl(): String = "http://192.168.1.100:8080"




}