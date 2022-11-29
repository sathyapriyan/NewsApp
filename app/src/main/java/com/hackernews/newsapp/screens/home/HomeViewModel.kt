package com.hackernews.newsapp.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackernews.newsapp.data.repository.HackerNewsRepository
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.util.ApiResponeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val hackerNewsRepository: HackerNewsRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _loadNewStoriesResponse: MutableLiveData<ApiResponeResult<List<ArticleResponse>>> =
        MutableLiveData(ApiResponeResult.Loading())
    val loadNewStoriesResponse: LiveData<ApiResponeResult<List<ArticleResponse>>> =
        _loadNewStoriesResponse

    fun onRefreshTriggered() {

        loadNewStories()

    }

    private fun loadNewStories() {

        _isRefreshing.value = true

        viewModelScope.launch(ioDispatcher) {

            val response =  hackerNewsRepository.getNewStories()

            response.collect {

                it.onSuccess { allStoriesEntityList ->

                    println(" Stories from DB --> $allStoriesEntityList")

                    if (allStoriesEntityList.isNotEmpty()) {

                        loadArticleItems(allStoriesEntityList[0]
                            .stories
                            .split(",")
                            .map {
                                it.trim().toInt()
                            })

                    } else {

                        hackerNewsRepository.getNewStories()

                    }

                }

                it.onFailure { throwable ->

                    _isRefreshing.value = false
                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }

    private fun loadArticleItems(articleItems: List<Int>) {

        println("List Article IDs --> $articleItems")

        viewModelScope.launch(ioDispatcher) {

            if (hackerNewsRepository.getArticleItemsCount() == -1) {

                articleItems.map {

                    hackerNewsRepository.saveArticleItem(articleId = it)

                }
            }

            val response = hackerNewsRepository.getArticleItem()

            response.collect {

                it.onSuccess {

                    if (it.isNotEmpty()) {

                        _isRefreshing.value = false
                        _loadNewStoriesResponse.postValue(ApiResponeResult.Success(it.subList(28,it.size)))

                    } else {

                        hackerNewsRepository.getArticleItem()

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