package com.hackernews.newsapp.screens.home

import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.navigation.Screens
import com.hackernews.newsapp.screens.components.TitleCard
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.RedRibbon
import com.hackernews.newsapp.ui.theme.TheNewsAppTheme
import com.hackernews.newsapp.ui.theme.screenBackgroundColor
import com.hackernews.newsapp.util.ApiResponeResult
import com.hackernews.newsapp.util.CommonUtil
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    // val viewModel = hiltViewModel<HomeViewModel>()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val newStoriesResponse = viewModel.loadNewStoriesResponse.observeAsState().value
    var articleResponseList = remember {
        emptyList<ArticleResponse>()
    }

    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colors.screenBackgroundColor

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing, onRefresh = viewModel::onRefreshTriggered
    )

    val coroutineScope = rememberCoroutineScope()

    var isDataLoading = remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current

    val pagerState = rememberPagerState(
        initialPage = 0
    )

    TheNewsAppTheme {

        SideEffect {

            systemUiController.setStatusBarColor(
                color = statusBarColor
            )

        }

        /*LaunchedEffect(Unit) {

            viewModel.loadNewStories()

        }*/

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

                val ( pullRefreshIndicator, circularProgress) = createRefs()

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

                        when (newStoriesResponse) {

                            is ApiResponeResult.Loading -> {

                                isDataLoading.value = true

                            }

                            is ApiResponeResult.Success -> {

                                isDataLoading.value = false

                                articleResponseList = newStoriesResponse.data!!

                                itemsIndexed(articleResponseList) { index, item ->

                                    TitleCard(
                                        modifier = Modifier,
                                        articleResponse = item,
                                        { parentId, comments ->

                                            if (comments?.isNotEmpty() == true) {

                                                navController.navigate(
                                                    Screens.Comments.passParentIdAndCommentList(
                                                        parentId = parentId,
                                                        commentsList = comments.toString()
                                                            .substring(
                                                                1,
                                                                comments.toString().length - 1
                                                            )
                                                    )
                                                )

                                            } else {

                                                CommonUtil.toastMessage(
                                                    context = context,
                                                    message = "No Comments"
                                                ).show()

                                            }

                                        },
                                        { url ->

                                            val encodedUrl = URLEncoder.encode(
                                                url,
                                                StandardCharsets.UTF_8.toString()
                                            )

                                            if (url != null) {

                                                navController.navigate(
                                                    route = Screens.Story.passUrl(url = encodedUrl)
                                                )

                                            } else {

                                                CommonUtil.toastMessage(
                                                    context = context,
                                                    message = "No URL found for this article!!"
                                                ).show()

                                            }

                                        }
                                    )

                                }

                            }

                            is ApiResponeResult.Error -> {

                                isDataLoading.value = false

                                item {

                                    Text(
                                        text = "${newStoriesResponse.message}"
                                    )

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