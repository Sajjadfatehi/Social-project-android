package com.example.nattramn.features.article.data

import com.example.nattramn.core.utils.ServiceBuilder
import com.example.nattramn.core.utils.safeApiCall
import com.example.nattramn.features.article.data.models.CommentRequest
import com.example.nattramn.features.article.services.ArticleApi

class ArticleRemoteDataSource {

    suspend fun getSingleArticle(slug: String) = safeApiCall {
        ServiceBuilder.buildService(ArticleApi::class.java).getSingleArticle(slug)
    }

    suspend fun bookmarkArticle(slug: String) = safeApiCall {
        ServiceBuilder.buildService(ArticleApi::class.java).bookmarkArticle(slug)
    }

    suspend fun removeFromBookmarks(slug: String) = safeApiCall {
        ServiceBuilder.buildService(ArticleApi::class.java).removeFromBookmarks(slug)
    }

    suspend fun sendComment(slug: String, commentRequest: CommentRequest) = safeApiCall {
        ServiceBuilder.buildService(ArticleApi::class.java).sendComment(slug, commentRequest)
    }

    suspend fun getArticleComments(slug: String) = safeApiCall {
        ServiceBuilder.buildService(ArticleApi::class.java).getArticleComments(slug)
    }

    suspend fun getTagArticles(tag: String) = safeApiCall {
        ServiceBuilder.buildService(ArticleApi::class.java).getTagArticles(tag)
    }

}