package com.hackernews.newsapp.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackernews.newsapp.R
import com.hackernews.newsapp.ui.theme.screenBackgroundColor
import com.hackernews.newsapp.ui.theme.textColor
import com.hackernews.newsapp.util.Constants.APP_NAME

@Composable
fun TopBarComp (
    backgroundColor: Color = MaterialTheme.colors.screenBackgroundColor,
    title: String = APP_NAME,
    titleColor: Color = MaterialTheme.colors.textColor,
    titleTxtSize: TextUnit = 24.sp,
    actions: @Composable () -> Unit
) {

    TopAppBar(
        backgroundColor = backgroundColor,
        title = {
            Row {

                Icon(
                    painter = painterResource(id = R.drawable.ic_logo),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(30.dp),
                    contentDescription = null )
                
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = title,
                    color = titleColor,
                    fontSize = titleTxtSize,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold)

            }
        },
        actions = {
            actions()
        }
    )

}