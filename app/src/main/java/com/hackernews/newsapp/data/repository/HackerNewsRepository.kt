package com.hackernews.newsapp.data.repository

import com.hackernews.newsapp.data.local.dao.HackerNewsDao
import com.hackernews.newsapp.data.local.entity.AllStoriesEntity
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

    fun getNewStories(storyType: Int = 1,refresh:Boolean=false): Flow<Result<List<AllStoriesEntity>>> {

        return hackerNewsDao
            .getAllStoriesList(storyType = storyType)
            .onEach {
                if (it.isEmpty() || refresh) {
                    saveNewStories()
                }
            }
            .map {

                Result.success(it)

            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getTopStories(storyType: Int = 2): Flow<Result<List<AllStoriesEntity>>> {

        return hackerNewsDao
            .getAllStoriesList(storyType = storyType)
            .onEach {
                if (it.isEmpty()) {
                    saveTopStories()
                }
            }
            .map {

                Result.success(it)

            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getBestStories(storyType: Int = 3): Flow<Result<List<AllStoriesEntity>>> {

        return hackerNewsDao
            .getAllStoriesList(storyType = storyType)
            .onEach {
                if (it.isEmpty()) {
                    saveBestStories()
                }
            }
            .map {

                Result.success(it)

            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getAllStories(storyType: Int,text: String = ""): Flow<Result<List<ArticleResponse>>> {


        val articleResponseList = mutableListOf<ArticleResponse>()
        println("getAllStories   --->  $text $storyType")

        return hackerNewsDao
            .getArticleItemStorySearch(storyType = storyType, text = "%$text%")
            .map {

                it.map { articleResponseEntity ->
                    articleResponseList.add(articleResponseEntity.toArticleResponse())
                }
                Result.success(articleResponseList)

            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getArticleItem(storyType: Int): Flow<Result<List<ArticleResponse>>> {

        val articleResponseList = mutableListOf<ArticleResponse>()

        return hackerNewsDao
            .getArticleItemStory(storyType = storyType)
            .map {

                it.map { articleResponseEntity ->
                    articleResponseList.add(articleResponseEntity.toArticleResponse())
                }
                Result.success(articleResponseList)

            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getComments(parentId: String): Flow<Result<List<ArticleResponse>>> {

        val commentsResponseList = mutableListOf<ArticleResponse>()

        return hackerNewsDao
            .getComments(parentId = parentId)
            .map {

                it.map { articleResponseEntity ->
                    commentsResponseList.add(articleResponseEntity.toArticleResponse())
                }
                Result.success(commentsResponseList)

            }
            .catch {
                emit(Result.failure(it))
            }

    }

    fun getArticleItemsCount(storyType: Int): Int = hackerNewsDao.getArticleItemsCount(storyType = storyType)

    fun getCommentsCountFromDB(parentId: String): Int = hackerNewsDao.getCommentsCount(parentId = parentId)



    private suspend fun saveNewStories() {

        hackerNewsApi.getNewStories().also { allStories ->

            if (hackerNewsDao.getDataCount(storyType = 1) == -1 || hackerNewsDao.getDataCount(storyType = 1) == 0) {

                hackerNewsDao.saveAllStories(
                    AllStoriesEntity(
                        0,
                        allStories.toString().substring(1,allStories.toString().length-1),
                        1))

            } else {

                val deleteResponse = hackerNewsDao.deleteStories(storyType = 1)

                if (deleteResponse != 0) {

                    hackerNewsDao.saveAllStories(
                        AllStoriesEntity(
                            0,
                            allStories.toString().substring(1,allStories.toString().length-1),
                            1))

                }

            }

        }

    }

    private suspend fun saveTopStories() {

        hackerNewsApi.getTopStories().also { allStories ->

            if (hackerNewsDao.getDataCount(storyType = 2) == -1 || hackerNewsDao.getDataCount(storyType = 2) == 0) {

                hackerNewsDao.saveAllStories(
                    AllStoriesEntity(
                        0,
                        allStories.toString().substring(1,allStories.toString().length-1),
                        2))

            } else {

                val deleteResponse = hackerNewsDao.deleteStories(storyType = 2)

                if (deleteResponse != 0) {

                    hackerNewsDao.saveAllStories(
                        AllStoriesEntity(
                            0,
                            allStories.toString().substring(1,allStories.toString().length-1),
                            2))

                }

            }

        }

    }

    private suspend fun saveBestStories() {

        hackerNewsApi.getBestStories().also { allStories ->

            if (hackerNewsDao.getDataCount(storyType = 3) == -1 || hackerNewsDao.getDataCount(storyType = 3) == 0) {

                hackerNewsDao.saveAllStories(
                    AllStoriesEntity(
                        0,
                        allStories.toString().substring(1,allStories.toString().length-1),
                        3))

            } else {

                val deleteResponse = hackerNewsDao.deleteStories(storyType = 3)

                if (deleteResponse != 0) {

                    hackerNewsDao.saveAllStories(
                        AllStoriesEntity(
                            0,
                            allStories.toString().substring(1,allStories.toString().length-1),
                            3))

                }

            }

        }

    }

    suspend fun saveArticleItem(articleId: Int, storyType: Int) {

        hackerNewsApi.getItem(articleId = articleId).also { articleResponse ->

            // val deleteResponse = hackerNewsDao.deleteDuplicateComments(parentId = articleId.toString())
             println("articleResponse  --> $articleResponse")

            hackerNewsDao.saveArticleItem(
                articleItem = articleResponse.toArticleResponseEntity(storyType = storyType)
            )

        }

    }

    suspend fun saveComment(articleId: Int) {

        hackerNewsApi.getItem(articleId = articleId).also { articleResponse ->

            // val deleteResponse = hackerNewsDao.deleteDuplicateComments(parentId = articleId.toString())
            println("articleResponse  --> $articleResponse")

            hackerNewsDao.saveArticleItem(
                articleItem = articleResponse.toArticleResponseEntity(storyType = 0)
            )

        }

    }

}