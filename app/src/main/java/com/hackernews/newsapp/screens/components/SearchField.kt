package com.hackernews.newsapp.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackernews.newsapp.R
import com.hackernews.newsapp.ui.theme.BlueLightLynch
import com.hackernews.newsapp.ui.theme.BlueVogue
import com.hackernews.newsapp.ui.theme.GrayAthens

@Composable
fun SearchField(
    modifier: Modifier,
    onTextChanged: (String) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = GrayAthens,
        shape = RoundedCornerShape(15.dp)
    ) {

        var text by remember {
            mutableStateOf(TextFieldValue(""))
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            label = null,
            placeholder = {
                Text(
                    text = "Search article",
                    color = BlueLightLynch,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    tint = BlueVogue
                )
            },
            trailingIcon = {
                if (text.text.isNotEmpty()) {

                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                text = TextFieldValue("")
                                onTextChanged("")
                            },
                        painter = painterResource(id = R.drawable.ic_clear_txt),
                        contentDescription = "Clear Icon",
                        tint = BlueVogue
                    )

                }
            },
            value = text.text,
            textStyle = TextStyle.Default.copy(fontSize = 14.sp),
            onValueChange = { newText ->
                text = TextFieldValue(newText)
                onTextChanged(newText)
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = GrayAthens,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = BlueVogue,
                disabledTextColor = BlueLightLynch
            )
        )

    }

}

@Preview
@Composable
fun SearchFieldPreview() {

    SearchField(
        modifier = Modifier,
        onTextChanged = {})

}