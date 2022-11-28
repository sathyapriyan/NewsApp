package com.hackernews.newsapp.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackernews.newsapp.data.local.entity.AllStoriesEntity
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

        viewModelScope.launch(ioDispatcher) {

            hackerNewsRepository.saveNewStories()

        }

    }

    fun loadNewStories() {

        _isRefreshing.value = true

        viewModelScope.launch(ioDispatcher) {

            val response = hackerNewsRepository.getNewStories()

            response.collect {

                it.onSuccess { allStoriesEntityList ->

                    val subList = allStoriesEntityList
                        .get(0)
                        .stories
                        .split(",")
                        .map {
                            it.trim().toInt()
                        }
                        .subList(0,10)

                    val tempList: MutableList<ArticleResponse> = mutableListOf()

                    subList.forEach { articleId ->

                        val response = hackerNewsRepository.getArticleItem(articleId = articleId)

                        response.collect {

                            it.onSuccess {

                                // tempList.addAll(it)
                                println("ViewModel Response --> $it")

                            }

                            it.onFailure { throwable ->

                                _isRefreshing.value = false
                                _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                            }

                        }

                    }

                    // _isRefreshing.value = false
                    // _loadNewStoriesResponse.postValue(ApiResponeResult.Success(tempList))

                }

                it.onFailure { throwable ->

                    _isRefreshing.value = false
                    _loadNewStoriesResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }


}