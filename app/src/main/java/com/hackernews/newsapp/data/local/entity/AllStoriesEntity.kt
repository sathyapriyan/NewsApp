package com.hackernews.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hackernews.newsapp.util.Constants.ALL_STORIES_TABLE


@Entity(tableName = ALL_STORIES_TABLE)
data class AllStoriesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var stories: String,
    var storyType: Int // 1.New 2.Top 3.Best
)
