package com.hackernews.newsapp.screens.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.GrayAthens
import com.hackernews.newsapp.R
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.ui.theme.BlueLightLynch
import com.hackernews.newsapp.util.CommonUtil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TitleCard(
    modifier: Modifier = Modifier,
    articleResponse: ArticleResponse,
    onClickComment: (String,String, List<Int>?) -> Unit,
    onClickCard: (String?) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clickable {
                onClickCard(articleResponse.url)
            },
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        elevation = 1.dp
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            val (titleTxt, btnComment, articleTime, cardScore,
                authorTxt) = createRefs()

            TextCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .constrainAs(titleTxt) {
                        top.linkTo(parent.top)
                        bottom.linkTo(btnComment.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = articleResponse.title!!,
                color = BlueVogue,
                size = 14.sp,
                fontWeight = FontWeight.Bold
            )

            CardIconWithText(
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(btnComment) {
                        top.linkTo(titleTxt.bottom)
                        bottom.linkTo(articleTime.top)
                        start.linkTo(titleTxt.start)
                        end.linkTo(cardScore.start)
                    }
                    .clickable {
                        onClickComment(articleResponse.title!!,articleResponse.id,articleResponse.kids)
                    },
                iconId = R.drawable.comment,
                strokeColor = GrayAthens,
                color = BlueLightLynch,
                text = if (articleResponse.kids?.size.toString() == "null") "0" else articleResponse.kids?.size.toString(),
                textSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            CardIconWithText(
                modifier = Modifier
                    .padding(5.dp)
                    .constrainAs(cardScore) {
                        top.linkTo(btnComment.top)
                        bottom.linkTo(btnComment.bottom)
                        start.linkTo(
                            btnComment.end,
                            margin = 10.dp
                        )
                    },
                iconId = R.drawable.ic_score,
                strokeColor = GrayAthens,
                color = BlueLightLynch,
                text = articleResponse.score!!,
                textSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            TextCard(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .constrainAs(articleTime) {
                        top.linkTo(btnComment.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(btnComment.start)
                    },
                text = CommonUtil.convertEpochToActualTime(articleResponse.time!!.toLong()),
                color = BlueLightLynch,
                size = 10.sp,
                fontWeight = FontWeight.Normal
            )

            TextCard(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .constrainAs(authorTxt) {
                        top.linkTo(articleTime.top)
                        bottom.linkTo(articleTime.bottom)
                        end.linkTo(parent.end)
                    },
                text = "- ${articleResponse.articleBy}",
                color = BlueLightLynch,
                size = 10.sp,
                fontWeight = FontWeight.Normal
            )

        }

    }

}

@Composable
fun TextCard(
    modifier: Modifier,
    text: String,
    color: Color,
    size: TextUnit,
    fontWeight: FontWeight
) {

    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontSize = size,
        fontWeight = fontWeight,
        textAlign = TextAlign.Start
    )

}

@Composable
fun CardIconWithText(
    modifier: Modifier,
    strokeColor: Color,
    iconId: Int,
    color: Color,
    text: String,
    textSize: TextUnit,
    fontWeight: FontWeight
) {

    Card(
        modifier = modifier
            .wrapContentSize(),
        backgroundColor = GrayAthens,
        border = BorderStroke(1.dp, strokeColor),
        shape = RoundedCornerShape(30.dp)
    ) {

        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(
                    start = 10.dp,
                    top = 2.dp,
                    end = 10.dp,
                    bottom = 2.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Icon(
                modifier = Modifier
                    .size(18.dp),
                painter = painterResource(id = iconId),
                contentDescription = "Comments",
                tint = color
            )

            TextCard(
                modifier = Modifier
                    .padding(5.dp),
                text = text,
                color = color,
                size = textSize,
                fontWeight = fontWeight
            )

        }

    }

}