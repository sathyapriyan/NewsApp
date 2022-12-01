package com.hackernews.newsapp.navigation

import com.hackernews.newsapp.util.Constants.COMMENTS_SCREEN_ROUTE
import com.hackernews.newsapp.util.Constants.HOME_SCREEN_ROUTE
import com.hackernews.newsapp.util.Constants.STORY_SCREEN_ROUTE

const val STORY_SCREEN_URL = "url"
const val COMMENTS_SCREEN_PARENT_ID = "parent_id"
const val COMMENTS_SCREENS_COMMENTS = "comments"

sealed class Screens(
    val route: String
) {

    object Home: Screens(HOME_SCREEN_ROUTE)
    object Story: Screens("$STORY_SCREEN_ROUTE/{$STORY_SCREEN_URL}"){

        fun passUrl(url: String): String {

            return this.route.replace(
                oldValue = "{$STORY_SCREEN_URL}",
                newValue = url
            )

        }

    }
    object Comments: Screens("$COMMENTS_SCREEN_ROUTE/{$COMMENTS_SCREEN_PARENT_ID}/{$COMMENTS_SCREENS_COMMENTS}") {

        fun passParentIdAndCommentList(parentId: String, commentsList: String): String {

            return "$COMMENTS_SCREEN_ROUTE/$parentId/$commentsList"

        }

    }

}
