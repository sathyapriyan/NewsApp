package com.hackernews.newsapp.di.modules

import android.content.Context
import androidx.room.Room
import com.hackernews.newsapp.data.local.dao.HackerNewsDao
import com.hackernews.newsapp.data.local.database.HackerNewsDatabase
import com.hackernews.newsapp.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideHackerNewsDatabase(
        @ApplicationContext context: Context
    ): HackerNewsDatabase {

        return Room.databaseBuilder(
            context,
            HackerNewsDatabase::class.java,
            DATABASE_NAME
        ).build()

    }

    @Singleton
    @Provides
    fun provideHackerNewsDao(
        hackerNewsDatabase: HackerNewsDatabase
    ): HackerNewsDao {

        return hackerNewsDatabase.hackerNewsDao()

    }

}