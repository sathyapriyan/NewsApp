package com.hackernews.newsapp.screens.comments

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val hackerNewsRepository: HackerNewsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext val context: Context
): ViewModel() {

    private val concurrentExceptionDelay = 1000L

    private val _loadCommentsResponse: MutableLiveData<ApiResponeResult<List<ArticleResponse>>> =
        MutableLiveData(ApiResponeResult.Loading())
    val loadCommentsResponse: LiveData<ApiResponeResult<List<ArticleResponse>>> =
        _loadCommentsResponse

    fun fetchCommentsItems(parentId: String, commentsItems: List<Int>) {

        _loadCommentsResponse.value = ApiResponeResult.Loading()

        println("List Comments IDs --> $commentsItems")

        viewModelScope.launch(ioDispatcher) {

            // Delay used to Concurrent List Exception
            delay(concurrentExceptionDelay)

            if (CommonUtil.hasInternetConnection(context = context)) {

                commentsItems.mapIndexed { index, value ->

                    if (index <= 50) {

                        println("Fetching comments...")

                        hackerNewsRepository.saveComment(articleId = value)

                    }

                }

                loadCommentsItems(parentId = parentId)

            } else {

                val commentsCountFromDB = hackerNewsRepository.getCommentsCountFromDB(parentId = parentId)
                println("commentsCountFromDB --> $commentsCountFromDB")

                if (commentsCountFromDB != 0) {

                    loadCommentsItems(parentId = parentId)

                } else {

                    _loadCommentsResponse.postValue(ApiResponeResult.Error("No Internet Connection"))

                }

            }

        }

    }

    private fun loadCommentsItems(parentId: String) {

        viewModelScope.launch(ioDispatcher) {

            val commentsResponse = hackerNewsRepository.getComments(parentId = parentId)

            commentsResponse.collect {

                println("Inside collecting comments...")

                it.onSuccess { articleResponseList ->

                    if (articleResponseList.isNotEmpty()) {

                        println("Before emitting response --> $it")

                        _loadCommentsResponse.postValue(ApiResponeResult.Success(articleResponseList))

                    } else {

                        hackerNewsRepository.getComments(parentId = parentId)

                    }

                }

                it.onFailure { throwable ->

                    _loadCommentsResponse.postValue(ApiResponeResult.Error(message = throwable.localizedMessage))

                }

            }

        }

    }

}