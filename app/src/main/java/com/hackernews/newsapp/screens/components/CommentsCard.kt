package com.hackernews.newsapp.screens.components

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hackernews.newsapp.R
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.ui.theme.BlueLightLynch
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.GrayAthens
import com.hackernews.newsapp.util.CommonUtil

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CommentsCard(
    modifier: Modifier = Modifier,
    commentResponse: ArticleResponse = ArticleResponse(
        articleBy = "vegasje",
        descendants = null,
        id = "33795563",
        kids = listOf(33795600),
        parent = "33795296",
        text = "This is incredible! I&#x27;m so excited to see what you do with this in the future.<p>May I ask what e-ink displays you&#x27;re using?",
        score = null,
        time = "1669770147",
        title = null,
        type = "comment",
        url = null
    )
) {

    var isLoadingMoreComments by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        elevation = 1.dp
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            val (imageCommentProfile, txtCommentBy, txtComment,
                commentTime, txtViewMoreComments,
                circularIndicatorLoadMoreComments) = createRefs()

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(5.dp)
                    .constrainAs(imageCommentProfile) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                painter = painterResource(id = R.drawable.ic_people),
                contentDescription = "Comment User",
                tint = BlueVogue
            )

            TextCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .constrainAs(txtCommentBy) {
                        top.linkTo(imageCommentProfile.top)
                        bottom.linkTo(imageCommentProfile.bottom)
                        start.linkTo(imageCommentProfile.end)
                    },
                text = commentResponse.articleBy ?: "",
                color = BlueVogue,
                size = 12.sp,
                fontWeight = FontWeight.Bold
            )

            TextCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .constrainAs(txtComment) {
                        top.linkTo(txtCommentBy.bottom)
                        bottom.linkTo(commentTime.top)
                        start.linkTo(txtCommentBy.start)
                        end.linkTo(parent.end)
                    },
                text = CommonUtil.htmlEntintyToString(
                    htmlEntityString = commentResponse.text!!
                ),
                color = BlueVogue,
                size = 12.sp,
                fontWeight = FontWeight.Normal
            )

            TextCard(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .constrainAs(commentTime) {
                        top.linkTo(txtComment.bottom)
                        bottom.linkTo(
                            txtViewMoreComments.top,
                            goneMargin = 20.dp
                        )
                        start.linkTo(txtCommentBy.start)
                    },
                text = CommonUtil.convertEpochToActualTime(commentResponse.time!!.toLong()) ?: "",
                color = BlueLightLynch,
                size = 10.sp,
                fontWeight = FontWeight.Normal
            )

            if (commentResponse.kids?.isNotEmpty() == true) {

                TextCard(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(5.dp)
                        .constrainAs(txtViewMoreComments) {
                            start.linkTo(txtCommentBy.start)
                            top.linkTo(commentTime.bottom)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickable {

                            isLoadingMoreComments = true

                            Handler(Looper.getMainLooper()).postDelayed({
                                isLoadingMoreComments = false
                            }, 5000)

                        },
                    text = "View ${commentResponse.kids?.size} more comments",
                    color = BlueVogue,
                    size = 10.sp,
                    fontWeight = FontWeight.Normal
                )

            }

            if (isLoadingMoreComments) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(10.dp)
                        .constrainAs(circularIndicatorLoadMoreComments) {

                            top.linkTo(txtViewMoreComments.top)
                            bottom.linkTo(txtViewMoreComments.bottom)
                            start.linkTo(txtViewMoreComments.end)

                        },
                    color = BlueVogue,
                    strokeWidth = 2.dp
                )

            }

        }

    }

}