package com.hackernews.newsapp.di.modules

import com.hackernews.newsapp.data.local.dao.HackerNewsDao
import com.hackernews.newsapp.data.local.database.HackerNewsDatabase
import com.hackernews.newsapp.data.remote.HackerNewsApi
import com.hackernews.newsapp.data.repository.HackerNewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideHackerNewsRepository(
        hackerNewsApi: HackerNewsApi,
        hackerNewsDao: HackerNewsDao
    ): HackerNewsRepository {

        return HackerNewsRepository(
            hackerNewsApi = hackerNewsApi,
            hackerNewsDao = hackerNewsDao
        )

    }

}