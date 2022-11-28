package com.hackernews.newsapp.util

import com.hackernews.newsapp.data.local.entity.ArticleResponseEntity
import com.hackernews.newsapp.model.ArticleResponse

object Mapper {

    fun ArticleResponse.toArticleResponseEntity(): ArticleResponseEntity {

        return ArticleResponseEntity(
            id = this.id,
            articleBy = this.articleBy,
            descendants = this.descendants,
            parent = this.parent,
            kids = this.kids.toString().substring(1,this.kids.toString().length-1),
            text = this.text,
            score = this.score,
            time = this.time,
            title = this.title,
            type = this.type,
            url = this.url
        )

    }

    fun ArticleResponseEntity.toArticleResponse(): ArticleResponse {

        return ArticleResponse(
            id = this.id,
            articleBy = this.articleBy,
            descendants = this.descendants,
            parent = this.parent,
            kids = this.kids?.split(",")?.map { it.toInt() },
            text = this.text,
            score = this.score,
            time = this.time,
            title = this.title,
            type = this.type,
            url = this.url
        )

    }

}