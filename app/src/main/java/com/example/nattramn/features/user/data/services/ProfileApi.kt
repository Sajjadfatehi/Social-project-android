package com.example.nattramn.features.user.data.services

import com.example.nattramn.features.home.data.models.ArticlesListResponse
import com.example.nattramn.features.user.data.models.UserProfileResponse
import retrofit2.Response
import retrofit2.http.*


interface ProfileApi {

    @GET("articles")
    suspend fun getUserArticles(@Query("author") username: String): ArticlesListResponse

    @GET("articles/favorite")
    suspend fun getBookmarkedArticles(@Query("favorited") username: String): ArticlesListResponse

    @GET("profiles/{username}")
    suspend fun getProfile(@Path("username") username: String): UserProfileResponse

    @DELETE("articles/{slug}")
    suspend fun deleteArticle(@Path("slug") slug: String): Response<Unit>?

    @POST("profiles/{username}/follow")
    suspend fun followUser(@Path("username") username: String): UserProfileResponse

    @DELETE("profiles/{username}/follow")
    suspend fun unFollowUser(@Path("username") username: String): UserProfileResponse

}