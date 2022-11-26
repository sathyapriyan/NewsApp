package com.hackernews.newsapp.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val RedRibbon = Color(0xFFFF2950)
val BlueVogue = Color(0xFF192E51)
val BlueLightLynch = Color(0xFF607698)
val GrayAthens = Color(0xFFE9E9F0)

val Colors.screenBackgroundColor
    get() = if (isLight) Color.White else Color.Black

val Colors.textColor
    get() = if (isLight) Color.Black else Color.White