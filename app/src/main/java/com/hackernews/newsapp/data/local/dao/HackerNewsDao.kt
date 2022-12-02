package com.hackernews.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackernews.newsapp.data.local.entity.AllStoriesEntity
import com.hackernews.newsapp.data.local.entity.ArticleResponseEntity
import com.hackernews.newsapp.util.Constants.TYPE_STORY
import kotlinx.coroutines.flow.Flow

@Dao
interface HackerNewsDao {

    @Query("SELECT * FROM ALL_STORIES_TABLE WHERE storyType = :storyType")
    fun getAllStoriesList(
        storyType: Int
    ): Flow<List<AllStoriesEntity>>

    @Query("SELECT * FROM STORY_TABLE WHERE type = :itemType AND storyType = :storyType ")
    fun getArticleItemStory(
        itemType: String = TYPE_STORY,
        storyType: Int
    ): Flow<List<ArticleResponseEntity>>

    @Query("SELECT * FROM STORY_TABLE WHERE type = :itemType AND storyType = :storyType  AND title LIKE :text")
    fun getArticleItemStorySearch(
        itemType: String = TYPE_STORY,
        storyType: Int,
        text:String
    ): Flow<List<ArticleResponseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllStories(allStoriesEntity: AllStoriesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticleItem(articleItem: ArticleResponseEntity)

    @Query("DELETE FROM ALL_STORIES_TABLE WHERE storyType = :storyType")
    fun deleteStories(storyType: Int): Int

    @Query("DELETE FROM STORY_TABLE WHERE parent = :parentId")
    fun deleteDuplicateComments(parentId: String): Int

    @Query("SELECT COUNT(*) FROM ALL_STORIES_TABLE WHERE storyType = :storyType")
    fun getDataCount(storyType: Int): Int

    @Query("SELECT COUNT(*) FROM STORY_TABLE  WHERE storyType = :storyType  ")
    fun getArticleItemsCount(storyType: Int): Int

    @Query("SELECT * FROM STORY_TABLE WHERE parent = :parentId")
    fun getComments(parentId: String): Flow<List<ArticleResponseEntity>>

    @Query("SELECT * FROM STORY_TABLE WHERE parent = :parentId")
    fun getCommentsCount(parentId: String): Int

}