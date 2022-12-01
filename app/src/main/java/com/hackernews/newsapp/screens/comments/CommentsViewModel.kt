package com.hackernews.newsapp.screens.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackernews.newsapp.data.repository.HackerNewsRepository
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.util.ApiResponeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val hackerNewsRepository: HackerNewsRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _loadCommentsResponse: MutableLiveData<ApiResponeResult<List<ArticleResponse>>> =
        MutableLiveData(ApiResponeResult.Loading())
    val loadCommentsResponse: LiveData<ApiResponeResult<List<ArticleResponse>>> =
        _loadCommentsResponse

    fun fetchCommentsItems(parentId: String, commentsItems: List<Int>) {

        _loadCommentsResponse.value = ApiResponeResult.Loading()

        println("List Comments IDs --> $commentsItems")

        viewModelScope.launch(ioDispatcher) {

            delay(1000)

            commentsItems.map {

                println("Fetching comments...")

                hackerNewsRepository.saveComment(articleId = it)

            }

            loadCommentsItems(parentId = parentId)

        }

    }

    fun loadCommentsItems(parentId: String) {

        viewModelScope.launch(ioDispatcher) {

            val commentsResponse = hackerNewsRepository.getComments(parentId = parentId)

            commentsResponse.collect {

                println("Inside collecting comments...")

                it.onSuccess {

                    if (it.isNotEmpty()) {

                        println("Before emitting response --> $it")

                        _loadCommentsResponse.postValue(ApiResponeResult.Success(it))

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