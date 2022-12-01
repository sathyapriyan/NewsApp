package com.hackernews.newsapp.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackernews.newsapp.ui.theme.BlueLightLynch
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.GrayAthens
import com.hackernews.newsapp.ui.theme.RedRibbon

@Composable
fun TabButton(
    modifier: Modifier,
    icon: Int,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
){

    Row(modifier = modifier
        .wrapContentSize()
        .padding(10.dp)
        .background(color = if (isSelected) BlueVogue else GrayAthens, shape = RoundedCornerShape(20.dp))
        .padding(
            start = 10.dp,
            end = 10.dp,
            top = 5.dp,
            bottom = 5.dp
        )
        .clickable {
                   onClick()
        },
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically) {

        Icon(modifier = Modifier.padding(5.dp)
            .size(18.dp),
            tint = if (isSelected) Color.White else BlueVogue,
            painter = painterResource(id = icon),
            contentDescription = "Tab Icon")

        Text(modifier = Modifier.padding(5.dp),
            text = text,
        color = if (isSelected) Color.White else BlueVogue,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold)

    }

}