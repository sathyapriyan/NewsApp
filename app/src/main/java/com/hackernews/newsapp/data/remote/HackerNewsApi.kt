package com.hackernews.newsapp.data.remote

import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.util.Constants.BEST_STORIES_URL
import com.hackernews.newsapp.util.Constants.NEW_STORIES_URL
import com.hackernews.newsapp.util.Constants.TOP_STORIES_URL
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsApi {

    @GET(NEW_STORIES_URL)
    suspend fun getNewStories(): List<Int>

    @GET(TOP_STORIES_URL)
    suspend fun getTopStories(): List<Int>

    @GET(BEST_STORIES_URL)
    suspend fun getBestStories(): List<Int>

    @GET("item/{articleId}.json?print=pretty")
    suspend fun getItem(
        @Path("articleId") articleId: Int
    ): ArticleResponse

}