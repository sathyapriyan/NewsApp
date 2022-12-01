package com.hackernews.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.hackernews.newsapp.util.Constants.STORY_TABLE

@Entity(tableName = STORY_TABLE)
data class ArticleResponseEntity(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    var id: String,

    @SerializedName("by")
    var articleBy: String?,

    @SerializedName("descendants")
    var descendants: String?,

    @SerializedName("parent")
    var parent: String?,

    @SerializedName("kids")
    var kids: String?,

    @SerializedName("text")
    var text: String?,

    @SerializedName("score")
    var score: String?,

    @SerializedName("time")
    var time: String?,

    @SerializedName("title")
    var title: String?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("url")
    var url: String?,

    var storyType: Int

)
