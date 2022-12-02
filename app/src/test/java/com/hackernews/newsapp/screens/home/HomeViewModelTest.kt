package com.hackernews.newsapp.screens.home

import android.content.Context
import com.hackernews.newsapp.data.repository.HackerNewsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.inject.Inject

@RunWith(JUnit4::class)
class HomeViewModelTest @Inject constructor(
    private val hackerNewsRepository: HackerNewsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) {

    private val homeViewModel = HomeViewModel(
        hackerNewsRepository = hackerNewsRepository,
        ioDispatcher = ioDispatcher,
        context = context
    )

    @Test
    fun commentsViewModel_FetchStories_LoadFromApi() {

        val currentHomeState = homeViewModel.loadNewStoriesResponse.value

        homeViewModel.loadNewStories(refresh = true)

    }

    @Test
    fun commentsViewModel_FetchStories_LoadFromDataBase() {

        val currentHomeState = homeViewModel.loadNewStoriesResponse.value

        homeViewModel.loadNewStories(refresh = false)

    }

}