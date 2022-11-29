package com.hackernews.newsapp.screens.home

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hackernews.newsapp.navigation.Screens
import com.hackernews.newsapp.screens.components.TitleCard
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.RedRibbon
import com.hackernews.newsapp.ui.theme.TheNewsAppTheme
import com.hackernews.newsapp.ui.theme.screenBackgroundColor
import com.hackernews.newsapp.util.ApiResponeResult
import com.hackernews.newsapp.util.Constants.STORY_SCREEN_ROUTE
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<HomeViewModel>()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val newStoriesResponse = viewModel.loadNewStoriesResponse.observeAsState().value

    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colors.screenBackgroundColor

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing, onRefresh = viewModel::onRefreshTriggered
    )

    var isDataLoading = remember {
        mutableStateOf(true)
    }

    TheNewsAppTheme {

        SideEffect {

            systemUiController.setStatusBarColor(
                color = statusBarColor
            )

        }

        Scaffold(
            topBar = { TopBarComp {} }
        ) { innerPadding ->

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colors.screenBackgroundColor)
                    .pullRefresh(pullRefreshState)
            ) {

                val (pullRefreshIndicator, circularProgress) = createRefs()

                if (isDataLoading.value) {

                    CircularProgressIndicator(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(circularProgress) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    content = {

                        when(newStoriesResponse) {

                            is ApiResponeResult.Loading -> {

                                isDataLoading.value = true

                            }

                            is ApiResponeResult.Success -> {

                                isDataLoading.value = false

                                items(count = 10) { i ->

                                    TitleCard(
                                        modifier = Modifier,
                                        title = newStoriesResponse.data?.get(i)?.title!!,
                                        commentCount = newStoriesResponse.data[i].kids?.size.toString(),
                                        scoreCount = newStoriesResponse.data[i].score!!,
                                        dateAndTime = Instant
                                            .ofEpochSecond(newStoriesResponse.data[i].time!!.toLong())
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime()
                                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"))
                                            .toString(),
                                        author = newStoriesResponse.data[i].articleBy!!,
                                        url = newStoriesResponse.data[i].url!!
                                    ) { url ->

                                        navController.navigate(
                                            route = "story_screen/$url"
                                        )

                                    }

                                }

                            }

                            is ApiResponeResult.Error -> {

                                isDataLoading.value = false

                                item {

                                    Text(
                                        text = "${newStoriesResponse.message}")

                                }

                            }

                        }

                    })

                PullRefreshIndicator(
                    modifier = Modifier
                        .constrainAs(pullRefreshIndicator) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    contentColor = RedRibbon,
                    backgroundColor = BlueVogue
                )

            }

        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    name = "DarkMode",
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "DarkMode",
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HomeScreenPreview() {

    TheNewsAppTheme {

        HomeScreen(navController = rememberNavController())

    }

}