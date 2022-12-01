package com.hackernews.newsapp.util

import com.hackernews.newsapp.data.local.entity.ArticleResponseEntity
import com.hackernews.newsapp.model.ArticleResponse

object Mapper {

    fun ArticleResponse.toArticleResponseEntity(storyType: Int): ArticleResponseEntity {

        return ArticleResponseEntity(
            id = this.id,
            articleBy = this.articleBy,
            descendants = this.descendants,
            parent = this.parent,
            kids = if (this.kids != null) this.kids.toString().substring(1,this.kids.toString().length-1) else null,
            text = this.text,
            score = this.score,
            time = this.time,
            title = this.title,
            type = this.type,
            url = this.url,
            storyType = storyType
        )

    }

    fun ArticleResponseEntity.toArticleResponse(): ArticleResponse {

        return ArticleResponse(
            id = this.id,
            articleBy = this.articleBy,
            descendants = this.descendants,
            parent = this.parent,
            kids = this.kids?.trim()?.split(",")?.map { it.trim().toInt()},
            text = this.text,
            score = this.score,
            time = this.time,
            title = this.title,
            type = this.type,
            url = this.url
        )

    }

}