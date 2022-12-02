package com.hackernews.newsapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.hackernews.newsapp.screens.components.SearchField
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.GrayAthens
import com.hackernews.newsapp.ui.theme.screenBackgroundColor
import com.hackernews.newsapp.ui.theme.textColor
import com.hackernews.newsapp.util.CommonUtil
import com.hackernews.newsapp.util.Constants.APP_NAME

@Composable
fun TopBarComp(
    backgroundColor: Color = MaterialTheme.colors.screenBackgroundColor,
    title: String = APP_NAME,
    titleColor: Color = BlueVogue,
    titleTxtSize: TextUnit = 24.sp,
    onTextChange: (String) -> Unit
) {

    var isSearchFieldVisible = remember {
        mutableStateOf(false)
    }

    TopAppBar(
        backgroundColor = backgroundColor,
        title = {
            if (isSearchFieldVisible.value) {

                SearchField(
                    modifier = Modifier
                        .padding(2.dp),
                    onTextChanged = {
                        onTextChange(it)
                    })

            } else {

                Row {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_logo),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(30.dp),
                        tint = if (isSystemInDarkTheme()) Color.White else BlueVogue,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = title,
                        color = if (isSystemInDarkTheme()) Color.White else BlueVogue,
                        fontSize = titleTxtSize,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )

                }

            }
        },
        actions = {
            // actions()

            if (isSearchFieldVisible.value) {

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 10.dp)
                        .clickable {
                            isSearchFieldVisible.value = false
                        },
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "TopBar Search",
                    tint = GrayAthens
                )

            } else {

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 10.dp)
                        .clickable {
                            isSearchFieldVisible.value = true
                        },
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "TopBar Search",
                    tint = GrayAthens
                )

            }

        }
    )

}