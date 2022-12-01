package com.hackernews.newsapp.screens.components.lazycolumn

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hackernews.newsapp.model.ArticleResponse
import com.hackernews.newsapp.screens.components.TitleCard

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InfiniteList(
    modifier: Modifier,
    listItems: List<ArticleResponse>,
    onLoadMore: () -> Unit,
    onClickComment: (String,List<Int>?) -> Unit,
    onClickItem: (String?) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        
        itemsIndexed(listItems) { index, item ->

            TitleCard(
                modifier = Modifier,
                articleResponse = item,
                { parentId, comments ->

                    onClickComment(parentId,comments)

                },
                { url ->

                    onClickItem(url)

                }
            )
            
        }
        
    }

    InfiniteListHandler(listState = listState) {
        onLoadMore()
    }
}