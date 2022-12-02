package com.hackernews.newsapp.screens.home

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hackernews.newsapp.R
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.navigation.Screens
import com.hackernews.newsapp.screens.components.TabButton
import com.hackernews.newsapp.screens.components.WarningIconText
import com.hackernews.newsapp.screens.components.lazycolumn.InfiniteList
import com.hackernews.newsapp.ui.theme.RedRibbon
import com.hackernews.newsapp.ui.theme.TheNewsAppTheme
import com.hackernews.newsapp.ui.theme.screenBackgroundColor
import com.hackernews.newsapp.util.ApiResponeResult
import com.hackernews.newsapp.util.CommonUtil
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    // val viewModel = hiltViewModel<HomeViewModel>()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val context = LocalContext.current

    val newStoriesResponse = viewModel.loadNewStoriesResponse.observeAsState().value
    var articleResponseList = remember {
        mutableStateListOf<ArticleResponse>()
    }

    var articleResponseSubList = remember {
        mutableStateListOf<ArticleResponse>()
    }

    var selectedTabButton by remember {
        mutableStateOf("new")
    }

    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colors.screenBackgroundColor

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing, onRefresh = {

            when (selectedTabButton) {
                "New" -> viewModel.loadNewStories(refresh = true)
                "Top" -> viewModel.loadTopStories(refresh = true)
                "Best" -> viewModel.loadBestStories(refresh = true)
                else -> {
                    viewModel.loadNewStories(refresh = true)
                }
            }

        }
    )

    val isDataLoading = remember {
        mutableStateOf(true)
    }

    var newTabButtonSelected by remember {
        mutableStateOf(true)
    }

    var topTabButtonSelected by remember {
        mutableStateOf(false)
    }

    var bestTabButtonSelected by remember {
        mutableStateOf(false)
    }

    TheNewsAppTheme {

        SideEffect {

            systemUiController.setStatusBarColor(
                color = statusBarColor
            )

        }

        LaunchedEffect(Unit) {

            viewModel.loadNewStories()

        }

        Scaffold(
            topBar = {
                TopBarComp(onTextChange = {

                    viewModel.loadSearchStories(
                        text = it, storyType = when (selectedTabButton) {
                            "New" -> 1
                            "Top" -> 2
                            "Best" -> 3
                            else -> {
                                1
                            }
                        }
                    )

                })
            }
        ) { innerPadding ->

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colors.screenBackgroundColor)
                    .pullRefresh(pullRefreshState)
            ) {

                val (tabRow, lazyColumStories,
                    pullRefreshIndicator, circularProgress,
                    warningText) = createRefs()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .constrainAs(tabRow) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(lazyColumStories.top)
                        },
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    TabButton(
                        modifier = Modifier,
                        icon = R.drawable.ic_new_news,
                        text = "New",
                        isSelected = newTabButtonSelected
                    ) {
                        selectedTabButton = "New"

                    }

                    TabButton(
                        modifier = Modifier,
                        icon = R.drawable.ic_top_news,
                        text = "Top",
                        isSelected = topTabButtonSelected
                    ) {
                        selectedTabButton = "Top"

                    }

                    TabButton(
                        modifier = Modifier,
                        icon = R.drawable.ic_score,
                        text = "Best",
                        isSelected = bestTabButtonSelected
                    ) {
                        selectedTabButton = "Best"

                    }

                }

                when (selectedTabButton) {

                    "New" -> {
                        LaunchedEffect(Unit) {

                            articleResponseList.clear()

                            println("selectedTabButton --> New Tab Btn")
                            viewModel.loadNewStories()
                            newTabButtonSelected = true
                            topTabButtonSelected = false
                            bestTabButtonSelected = false

                        }
                    }

                    "Top" -> {
                        LaunchedEffect(Unit) {

                            articleResponseList.clear()

                            viewModel.loadTopStories()
                            println("selectedTabButton --> Top Tab Btn")
                            newTabButtonSelected = false
                            topTabButtonSelected = true
                            bestTabButtonSelected = false

                        }
                    }

                    "Best" -> {

                        LaunchedEffect(Unit) {

                            articleResponseList.clear()

                            viewModel.loadBestStories()
                            println("selectedTabButton --> Best Tab Btn")
                            newTabButtonSelected = false
                            topTabButtonSelected = false
                            bestTabButtonSelected = true

                        }

                    }

                }

                if (isDataLoading.value) {

                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .constrainAs(circularProgress) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        color = RedRibbon
                    )

                }

                when (newStoriesResponse) {

                    is ApiResponeResult.Loading -> {

                        isDataLoading.value = true

                    }

                    is ApiResponeResult.Success -> {

                        isDataLoading.value = false

                        articleResponseList = newStoriesResponse.data!!.toMutableStateList()

                        articleResponseSubList = if (articleResponseList.size >= 5) {
                            articleResponseList
                                .subList(
                                    0,
                                    if (articleResponseList.size >= articleResponseList.size + 11) {
                                        articleResponseSubList.size + 10
                                    } else {
                                        articleResponseList.size - 1
                                    }
                                )
                                .toMutableStateList()
                        } else {
                            articleResponseList
                        }

                        println("Lazy Column --> On Load")
                        println("Lazy Column Lis articleResponseList -->  ${articleResponseList.toList()}")

                        println("Lazy Column Lis articleResponseSubList -->  ${articleResponseSubList.toList()}")

                        InfiniteList(
                            modifier = Modifier
                                .constrainAs(lazyColumStories) {
                                    top.linkTo(tabRow.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                }, articleResponseSubList,
                            onClickItem = { url ->

                                if (url != null) {

                                    if (CommonUtil.hasInternetConnection(context = context)) {

                                        val encodedUrl =
                                            URLEncoder.encode(
                                                url,
                                                StandardCharsets.UTF_8.toString()
                                            )

                                        navController.navigate(
                                            route = Screens.Story.passUrl(url = encodedUrl)
                                        )

                                    } else {

                                        CommonUtil.toastMessage(
                                            context = context,
                                            message = "No Internet !!"
                                        ).show()

                                    }

                                } else {

                                    CommonUtil.toastMessage(
                                        context = context,
                                        message = "No URL found for this article!!"
                                    ).show()

                                }

                            },
                            onClickComment = { title, parentId, comments ->

                                println("Lazy Column --> LoadMore Triggered")

                                if (comments?.isNotEmpty() == true) {

                                    navController.navigate(
                                        Screens.Comments.passTitleParentIdAndCommentList(
                                            title = title,
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
                            onLoadMore = {

                                println("Lazy Column --> On Load More")


                                if (articleResponseSubList.size >= 5) {
                                    articleResponseSubList
                                        .addAll(
                                            articleResponseList
                                                .subList(
                                                    articleResponseSubList.size - 1,
                                                    if (articleResponseList.size >= articleResponseList.size + 11) {
                                                        articleResponseSubList.size + 10
                                                    } else {
                                                        articleResponseList.size
                                                    }
                                                )
                                        )

                                }

                            })

                    }

                    is ApiResponeResult.Error -> {

                        isDataLoading.value = false

                        if (newStoriesResponse.message?.contains("Unable to resolve host") == true
                            || newStoriesResponse.message?.contains("No Internet Connection") == true) {

                            WarningIconText(
                                modifier = Modifier.constrainAs(warningText) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                                text = "No Internet",
                                icon = R.drawable.ic_no_internet
                            )

                        } else {

                            Text(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .constrainAs(warningText) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                    },
                                text = "${newStoriesResponse.message}",
                                color = RedRibbon
                            )

                        }

                    }

                    else -> {}
                }

                PullRefreshIndicator(
                    modifier = Modifier
                        .constrainAs(pullRefreshIndicator) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    contentColor = RedRibbon
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