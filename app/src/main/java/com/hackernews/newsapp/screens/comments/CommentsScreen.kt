package com.hackernews.newsapp.screens.comments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hackernews.newsapp.R
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.screens.components.CommentsCard
import com.hackernews.newsapp.screens.components.TextCard
import com.hackernews.newsapp.screens.components.WarningIconText
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.RedRibbon
import com.hackernews.newsapp.ui.theme.TheNewsAppTheme
import com.hackernews.newsapp.ui.theme.screenBackgroundColor
import com.hackernews.newsapp.util.ApiResponeResult

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommentsScreen(
    title: String,
    parentId: String,
    comments: List<Int>,
    viewModel: CommentsViewModel = hiltViewModel()
) {

    // val viewModel = hiltViewModel<CommentsViewModel>()

    val isDataLoading = remember {
        mutableStateOf(true)
    }

    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colors.screenBackgroundColor

    val commentsResponse = viewModel.loadCommentsResponse.observeAsState().value
    var commentsResponseList = remember {
        mutableListOf<ArticleResponse>()
    }

    TheNewsAppTheme {

        SideEffect {

            systemUiController.setStatusBarColor(
                color = statusBarColor
            )

        }

        LaunchedEffect(Unit) {

            viewModel.fetchCommentsItems(
                parentId = parentId,
                commentsItems = comments
            )

        }

        Scaffold { innerPadding ->

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colors.screenBackgroundColor)
            ) {

                val (txtTitle,txtHeading, circularProgress, lazyColumnComments) = createRefs()

                TextCard(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp)
                        .constrainAs(txtHeading) {
                            top.linkTo(parent.top)
                            bottom.linkTo(txtTitle.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    text = "Comments",
                    color = if (isSystemInDarkTheme()) Color.White else BlueVogue,
                    size = 20.sp,
                    fontWeight = FontWeight.Normal
                )

                TextCard(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp)
                        .constrainAs(txtTitle) {
                            top.linkTo(txtHeading.bottom)
                            bottom.linkTo(lazyColumnComments.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    text = title,
                    color = if (isSystemInDarkTheme()) Color.White else BlueVogue,
                    size = 20.sp,
                    fontWeight = FontWeight.Bold
                )

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

                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .constrainAs(lazyColumnComments) {
                            top.linkTo(txtTitle.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    content = {

                        when (commentsResponse) {

                            is ApiResponeResult.Loading -> {

                                isDataLoading.value = true

                            }

                            is ApiResponeResult.Success -> {

                                isDataLoading.value = false

                                println("commentsResponseList Before --> ${commentsResponseList.size}")

                                commentsResponseList.clear()

                                println("commentsResponseList After clear --> ${commentsResponseList.size}")

                                commentsResponseList = commentsResponse.data!!.toMutableList()

                                println("commentsResponseList After --> ${commentsResponseList.size}")

                                itemsIndexed(commentsResponseList) { _, item ->

                                    CommentsCard(
                                        modifier = Modifier,
                                        commentResponse = item
                                    )

                                }

                            }

                            is ApiResponeResult.Error -> {

                                isDataLoading.value = false

                                item {

                                    if (commentsResponse.message?.contains("Unable to resolve host") == true
                                        || commentsResponse.message?.contains("No Internet Connection") == true) {

                                        Column(modifier = Modifier
                                            .fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center) {

                                            WarningIconText(
                                                modifier = Modifier,
                                                text = "No Internet",
                                                icon = R.drawable.ic_no_internet
                                            )

                                        }

                                    } else {

                                        Column(modifier = Modifier
                                            .fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center) {

                                            Text(
                                                modifier = Modifier
                                                    .wrapContentSize(),
                                                text = "${commentsResponse.message}",
                                                color = RedRibbon
                                            )

                                        }

                                    }

                                }

                            }

                            else -> {}
                        }

                    })

            }

        }

    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CommentsScreenPreview() {

    TheNewsAppTheme {

        CommentsScreen("Title","7787878",listOf())

    }

}