package com.hackernews.newsapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hackernews.newsapp.screens.comments.CommentsScreen
import com.hackernews.newsapp.screens.home.HomeScreen
import com.hackernews.newsapp.screens.story.StoryScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {

        composable(route = Screens.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screens.Story.route,
            arguments = listOf(navArgument(
                STORY_SCREEN_URL
            ) {
                type = NavType.StringType
            })
        ) {

            println("URL Value --> ${it.arguments?.getString(STORY_SCREEN_URL)}")

            StoryScreen(
                navController = navController,
                url = it.arguments?.getString(STORY_SCREEN_URL)!!
            )
        }

        composable(
            route = Screens.Comments.route,
            arguments = listOf(
                navArgument(COMMENTS_SCREEN_PARENT_ID) { type = NavType.StringType },
                navArgument(COMMENTS_SCREENS_COMMENTS) { type = NavType.StringType }
            )) {

            val commentsList = it.arguments?.getString(COMMENTS_SCREENS_COMMENTS)!!.trim().split(",").map { it.trim().toInt()}

            println("commentsList NavGraph --> ${it.arguments?.getString(COMMENTS_SCREENS_COMMENTS)}")
            println("commentsList NavGraph2 --> $commentsList")

            CommentsScreen(
                navController = navController,
                parentId = it.arguments?.getString(COMMENTS_SCREEN_PARENT_ID)!!,
                comments = commentsList
            )
        }

    }

}