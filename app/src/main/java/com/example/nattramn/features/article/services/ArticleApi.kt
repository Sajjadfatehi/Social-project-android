package com.example.nattramn.features.article.services

import com.example.nattramn.features.article.data.models.ArticleComments
import com.example.nattramn.features.article.data.models.CommentRequest
import com.example.nattramn.features.home.data.models.ArticlesListResponse
import com.example.nattramn.features.home.data.models.SingleArticleResponse
import retrofit2.http.*


interface ArticleApi {

    @GET("articles/{slug}")
    suspend fun getSingleArticle(@Path("slug") slug: String): SingleArticleResponse

    @POST("articles/{slug}/favorite")
    suspend fun bookmarkArticle(@Path("slug") slug: String): SingleArticleResponse

    @DELETE("articles/{slug}/favorite")
    suspend fun removeFromBookmarks(@Path("slug") slug: String): SingleArticleResponse

    @POST("articles/{slug}/comments")
    suspend fun sendComment(@Path("slug") slug: String, @Body commentRequest: CommentRequest)

    @GET("articles/{slug}/comments")
    suspend fun getArticleComments(@Path("slug") slug: String): ArticleComments

    @GET("articles/tag")
    suspend fun getTagArticles(@Query("tag") tag: String): ArticlesListResponse

}
