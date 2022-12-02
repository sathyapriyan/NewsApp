package com.hackernews.newsapp.screens.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackernews.newsapp.data.repository.HackerNewsRepository
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.util.ApiResponeResult
import com.hackernews.newsapp.util.CommonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val hackerNewsRepository: HackerNewsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _loadNewStoriesResponse: MutableLiveData<ApiResponeResult<MutableList<ArticleResponse>>> =
        MutableLiveData(ApiResponeResult.Loading())
    val loadNewStoriesResponse: LiveData<ApiResponeResult<MutableList<ArticleResponse>>> =
        _loadNewStoriesResponse

    fun loadNewStories(refresh:Boolean=false) {

        if (refresh) {

            _isRefreshing.value = true

        }

        viewModelScope.launch(ioDispatcher) {

            val response =  hackerNewsRepository.getNewStories()

            response.collect {

                it.onSuccess { allStoriesEntityList ->

                    println(" Stories from DB --> $allStoriesEntityList")

                    if (allStoriesEntityList.isNotEmpty()) {

                        loadArticleItems(allStoriesEntityList[0]
                            .stories
                            .split(",")
                            .map { string ->
                                string.trim().toInt()
                            },1,refresh)

                    } else {

                        hackerNewsRepository.getNewStories()

                    }

                }

                it.onFailure { throwable ->

                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }

    fun loadTopStories(refresh:Boolean=false) {

        if (refresh) {

            _isRefreshing.value = true

        }

        viewModelScope.launch(ioDispatcher) {

            val response =  hackerNewsRepository.getTopStories()

            response.collect {

                it.onSuccess { allStoriesEntityList ->

                    println(" Stories from DB --> $allStoriesEntityList")

                    if (allStoriesEntityList.isNotEmpty()) {

                        loadArticleItems(allStoriesEntityList[0]
                            .stories
                            .split(",")
                            .map { string ->
                                string.trim().toInt()
                            },2,refresh)

                    } else {

                        hackerNewsRepository.getTopStories()

                    }

                }

                it.onFailure { throwable ->

                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }

    fun loadBestStories(refresh:Boolean=false) {

        if (refresh) {

            _isRefreshing.value = true

        }

        viewModelScope.launch(ioDispatcher) {

            val response =  hackerNewsRepository.getBestStories()

            response.collect {

                it.onSuccess { allStoriesEntityList ->

                    println(" Stories from DB --> $allStoriesEntityList")

                    if (allStoriesEntityList.isNotEmpty()) {

                        loadArticleItems(allStoriesEntityList[0]
                            .stories
                            .split(",")
                            .map { string ->
                                string.trim().toInt()
                            },3,refresh)

                    } else {

                        hackerNewsRepository.getBestStories()

                    }

                }

                it.onFailure { throwable ->

                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }

    fun loadSearchStories(storyType:Int, text:String="") {

        viewModelScope.launch(ioDispatcher) {

            val response =  hackerNewsRepository.getAllStories(storyType = storyType, text = text)

            response.collect {

                it.onSuccess { allStoriesEntityList ->

                    println(" Stories from DB --> $allStoriesEntityList")
                    if (allStoriesEntityList.isNotEmpty()) {

                        _loadNewStoriesResponse.postValue(ApiResponeResult.Success(allStoriesEntityList.toMutableList()))

                    } else {

                        hackerNewsRepository.getAllStories(storyType = storyType, text = text)

                    }




                }

                it.onFailure { throwable ->

                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }

    private fun loadArticleItems(articleItems: List<Int>, storyType:Int, refresh:Boolean=false) {

        println("List Article IDs --> $articleItems")

        viewModelScope.launch(ioDispatcher) {

            val articleItemCount = hackerNewsRepository.getArticleItemsCount(storyType= storyType)

            println("Article Item Count --> $articleItemCount")

            if ( articleItemCount == 0 || refresh) {

                println("Inside fetching data")

                if (CommonUtil.hasInternetConnection(context = context)) {

                    articleItems.mapIndexed { index, value ->

                        if (index <= articleItems.size/5) {

                            println("Fetching data...")

                            hackerNewsRepository.saveArticleItem(articleId = value, storyType = storyType)

                        }

                    }

                } else {

                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error("No Internet Connection"))

                }
            }

            val response = hackerNewsRepository.getArticleItem(storyType = storyType)

            response.collect {

                println("Inside collecting data")

                it.onSuccess { articleResponseList ->

                    if (articleResponseList.isNotEmpty()) {

                        _isRefreshing.value = false

                        _loadNewStoriesResponse.postValue(ApiResponeResult.Success(articleResponseList.toMutableList()))

                    } else {

                        hackerNewsRepository.getArticleItem(storyType = storyType)

                    }

                }

                it.onFailure { throwable ->

                    _isRefreshing.value = false

                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }

}