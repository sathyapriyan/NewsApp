package com.hackernews.newsapp.data.repository

import com.hackernews.newsapp.data.local.dao.HackerNewsDao
import com.hackernews.newsapp.data.local.entity.AllStoriesEntity
import com.hackernews.newsapp.data.local.entity.ArticleResponseEntity
import com.hackernews.newsapp.data.remote.HackerNewsApi
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.util.Mapper.toArticleResponse
import com.hackernews.newsapp.util.Mapper.toArticleResponseEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HackerNewsRepository @Inject constructor(
    val hackerNewsApi: HackerNewsApi,
    val hackerNewsDao: HackerNewsDao
) {

    fun getNewStories(storyType: Int = 1): Flow<Result<List<AllStoriesEntity>>> {

        return hackerNewsDao
            .getAllStoriesList(storyType = storyType)
            .map {

                Result.success(it)

            }
            .onEach {
                if (it.getOrDefault(emptyList()).isEmpty()) {
                    saveNewStories()
                }
            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getTopStories(storyType: Int = 2): Flow<Result<List<AllStoriesEntity>>> {

        return hackerNewsDao
            .getAllStoriesList(storyType = storyType)
            .map {

                Result.success(it)

            }
            .onEach {
                if (it.getOrDefault(emptyList()).isEmpty()) {
                    saveTopStories()
                }
            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getBestStories(storyType: Int = 3): Flow<Result<List<AllStoriesEntity>>> {

        return hackerNewsDao
            .getAllStoriesList(storyType = storyType)
            .map {

                Result.success(it)

            }
            .onEach {
                if (it.getOrDefault(emptyList()).isEmpty()) {
                    saveBestStories()
                }
            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getArticleItem(
        articleId: Int
    ): Flow<Result<List<ArticleResponse>>> {

        val articleResponseList = mutableListOf<ArticleResponse>()

        return hackerNewsDao
            .getArticleItemStory()
            .map {

                it.map {
                    articleResponseList.add(it.toArticleResponse())
                }
                Result.success(articleResponseList)

            }
            .onEach {

                if (it.getOrDefault(emptyList()).isEmpty()) {

                    saveArticleItem(articleId = articleId)

                }

            }
            .catch {
                emit(Result.failure(it))
            }

    }

    suspend fun saveNewStories() {

        hackerNewsApi.getNewStories().also { allStories ->

            hackerNewsDao.saveAllStories(
                AllStoriesEntity(
                    0,
                    allStories.toString().substring(1,allStories.toString().length-1),
                    1))

        }

    }

    suspend fun saveTopStories() {

        hackerNewsApi.getTopStories().also { allStories ->

            hackerNewsDao.saveAllStories(
                AllStoriesEntity(
                    0,
                    allStories.toString().substring(1,allStories.toString().length-1),
                    2))

        }



    }

    suspend fun saveBestStories() {

        hackerNewsApi.getBestStories().also { allStories ->

            hackerNewsDao.saveAllStories(
                AllStoriesEntity(
                    0,
                    allStories.toString().substring(1,allStories.toString().length-1),
                    3))

        }



    }

    suspend fun saveArticleItem(articleId: Int) {

        hackerNewsApi.getItem(articleId = articleId).also { articleResponse ->

            hackerNewsDao.saveArticleItem(
                articleItem = articleResponse.toArticleResponseEntity()
            )

        }

    }

}