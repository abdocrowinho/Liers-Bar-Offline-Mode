package com.example.data.DataSource.localeDataSource

import com.example.domain.Repo.GamePlayRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamePlayProvider{
    @Provides
    @Singleton
    fun providesGamePlayRepo() : GamePlayRepo {
        return GamePlayRepoImpl()
    }
}