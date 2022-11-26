package com.hackernews.newsapp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hackernews.newsapp.ui.theme.TheNewsAppTheme
import com.hackernews.newsapp.ui.theme.screenBackgroundColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colors.screenBackgroundColor

    val pullRefreshState = rememberPullRefreshState(
        refreshing = false, onRefresh = { })

    TheNewsAppTheme {

        SideEffect {

            systemUiController.setStatusBarColor(
                color = statusBarColor
            )

        }
        
        Scaffold(
            topBar =  {

                TopBarComp {

                }

            }) { innerPadding ->
            
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colors.screenBackgroundColor)
                    .pullRefresh(pullRefreshState)
                    .pullRefreshIndicatorTransform(pullRefreshState)
            ) {

            }

        }

    }

}