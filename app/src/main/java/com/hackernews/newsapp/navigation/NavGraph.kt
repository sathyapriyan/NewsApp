package com.hackernews.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hackernews.newsapp.screens.comments.CommentsScreen
import com.hackernews.newsapp.screens.home.HomeScreen
import com.hackernews.newsapp.screens.story.StoryScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {

    NavHost(
        navController =navController,
        startDestination = Screens.Home.route) {

        composable(route = Screens.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Screens.Story.route) {
            StoryScreen(navController = navController)
        }

        composable(route = Screens.Comments.route) {
            CommentsScreen(navController = navController)
        }

    }

}