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
            route = "story_screen/?url={url}", // "story_screen/{url}"   Screens.Story.route
            arguments = listOf(navArgument(
                "url"
            ) {
                type = NavType.StringType
            })
        ) {
            StoryScreen(
                navController = navController,
                url = it.arguments?.getString("url")!!
            )
        }

        composable(route = Screens.Comments.route) {
            CommentsScreen(navController = navController)
        }

    }

}