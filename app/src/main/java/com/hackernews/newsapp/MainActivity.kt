package com.hackernews.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.hackernews.newsapp.navigation.SetupNavGraph
import com.hackernews.newsapp.ui.theme.TheNewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheNewsAppTheme {

                val navController = rememberNavController()
                SetupNavGraph(navController = navController)

            }
        }
    }
}