package com.hackernews.newsapp.util

object Constants {

    const val APP_NAME = "NewsApp"

    // Screen Routes
    const val HOME_SCREEN_ROUTE = "home_screen"
    const val STORY_SCREEN_ROUTE = "story_screen"
    const val COMMENTS_SCREEN_ROUTE = "comments_screen"

    // Database Constants
    const val DATABASE_NAME = "hackernews_database"
    const val DATABASE_VERSION = 1
    const val ALL_STORIES_TABLE = "all_stories_table"
    const val STORY_TABLE = "story_table"
    const val TYPE_STORY = "story"
    const val TYPE_COMMENT = "comment"

    // API Constants
    const val JSON_PRINT_QUERY_KEY = "print"
    const val JSON_PRINT_FORMAT = "pretty"
    const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"
    const val NEW_STORIES_URL = "newstories.json?print=pretty"
    const val TOP_STORIES_URL = "topstories.json?print=pretty"
    const val BEST_STORIES_URL = "beststories.json?print=pretty"

}