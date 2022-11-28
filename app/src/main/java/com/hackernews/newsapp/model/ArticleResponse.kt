package com.hackernews.newsapp.model

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

    @SerializedName("by")
    var articleBy: String?,

    @SerializedName("descendants")
    var descendants: String?,

    @SerializedName("id")
    var id: String,

    @SerializedName("parent")
    var parent: String?,

    @SerializedName("kids")
    var kids: List<Int>?,

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
    var url: String?

)
