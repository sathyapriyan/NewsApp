package com.hackernews.newsapp.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hackernews.newsapp.R
import com.hackernews.newsapp.ui.theme.RedRibbon

@Composable
fun WarningIconText(
    modifier: Modifier,
    text: String,
    icon: Int = R.drawable.ic_no_internet
) {

    Column(modifier = modifier
        .padding(10.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceAround) {

        Box(modifier = Modifier
            .wrapContentSize()
            .background(
                color = RedRibbon.copy(alpha = 0.1f),
            shape = CircleShape)
            .padding(10.dp)) {

            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Warning Text",
            tint = RedRibbon)

        }

        Text(
            text = text,
        color = RedRibbon)

    }

}

@Preview
@Composable
fun WarningIconTextPreview() {

    WarningIconText(
        modifier = Modifier,
        text ="No Internet",
    icon = R.drawable.ic_no_internet)

}