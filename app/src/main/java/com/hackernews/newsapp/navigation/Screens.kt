package com.hackernews.newsapp.navigation

import com.hackernews.newsapp.util.Constants.COMMENTS_SCREEN_ROUTE
import com.hackernews.newsapp.util.Constants.HOME_SCREEN_ROUTE
import com.hackernews.newsapp.util.Constants.STORY_SCREEN_ROUTE

sealed class Screens(
    val route: String
) {

    object Home: Screens(HOME_SCREEN_ROUTE)
    object Story: Screens(STORY_SCREEN_ROUTE)
    object Comments: Screens(COMMENTS_SCREEN_ROUTE)

}
