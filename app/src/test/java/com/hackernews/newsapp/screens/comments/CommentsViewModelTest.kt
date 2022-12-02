package com.hackernews.newsapp.screens.comments

import android.content.Context
import com.hackernews.newsapp.data.repository.HackerNewsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.inject.Inject

@RunWith(JUnit4::class)
class CommentsViewModelTest @Inject constructor(
    private val hackerNewsRepository: HackerNewsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) {

    private val commentsViewModel = CommentsViewModel(
        hackerNewsRepository = hackerNewsRepository,
        ioDispatcher = ioDispatcher,
        context = context
    )



}