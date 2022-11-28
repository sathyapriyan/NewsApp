package com.hackernews.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hackernews.newsapp.data.local.dao.HackerNewsDao
import com.hackernews.newsapp.data.local.entity.AllStoriesEntity
import com.hackernews.newsapp.data.local.entity.ArticleResponseEntity
import com.hackernews.newsapp.util.Constants.DATABASE_VERSION

@Database(
    entities = [AllStoriesEntity::class, ArticleResponseEntity::class],
    version = DATABASE_VERSION
)
abstract class HackerNewsDatabase: RoomDatabase() {

    abstract fun hackerNewsDao(): HackerNewsDao

}