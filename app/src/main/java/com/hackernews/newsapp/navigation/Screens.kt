package com.hackernews.newsapp.navigation

import com.hackernews.newsapp.util.Constants.COMMENTS_SCREEN_ROUTE
import com.hackernews.newsapp.util.Constants.HOME_SCREEN_ROUTE
import com.hackernews.newsapp.util.Constants.STORY_SCREEN_ROUTE

const val STORY_SCREEN_URL = "url"

sealed class Screens(
    val route: String
) {

    object Home: Screens(HOME_SCREEN_ROUTE)
    object Story: Screens("story_screen/{url}"){ // $STORY_SCREEN_ROUTE/{$STORY_SCREEN_URL}

        fun passUrl(url: String): String {

            return this.route.replace(
                oldValue = "{$STORY_SCREEN_URL}",
                newValue = url
            )

        }

    }
    object Comments: Screens(COMMENTS_SCREEN_ROUTE)

}
