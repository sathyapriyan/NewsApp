package com.hackernews.newsapp.screens.comments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.navigation.Screens
import com.hackernews.newsapp.screens.components.CommentsCard
import com.hackernews.newsapp.screens.components.TextCard
import com.hackernews.newsapp.screens.components.TitleCard
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.TheNewsAppTheme
import com.hackernews.newsapp.ui.theme.screenBackgroundColor
import com.hackernews.newsapp.util.ApiResponeResult
import com.hackernews.newsapp.util.CommonUtil
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommentsScreen(
    navController: NavHostController,
    parentId: String,
    comments: List<Int>,
    viewModel: CommentsViewModel = hiltViewModel()
) {

    // val viewModel = hiltViewModel<CommentsViewModel>()

    val isDataLoading = remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current

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

        Scaffold() { innerPadding ->

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colors.screenBackgroundColor)
            ) {

                val (txtTitle, circularProgress, lazyColumnComments) = createRefs()

                TextCard(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(10.dp)
                        .constrainAs(txtTitle) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    text = "Comments",
                    color = BlueVogue,
                    size = 20.sp,
                    fontWeight = FontWeight.Bold
                )

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

                                itemsIndexed(commentsResponseList) { index, item ->

                                    CommentsCard(
                                        modifier = Modifier,
                                        commentResponse = item
                                    )

                                }

                            }

                            is ApiResponeResult.Error -> {

                                isDataLoading.value = false

                                item {

                                    Text(
                                        text = "${commentsResponse.message}"
                                    )

                                }

                            }

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

        CommentsScreen(rememberNavController(),"7787878",listOf())

    }

}