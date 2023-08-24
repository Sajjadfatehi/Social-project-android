package com.example.nattramn.features.home.data.models

import com.example.nattramn.R
import com.example.nattramn.core.config.MyApp
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.article.data.entities.TagEntity
import com.example.nattramn.features.article.data.models.ArticleComments
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.article.ui.CommentView
import com.example.nattramn.features.user.ui.UserView
import com.google.gson.annotations.SerializedName

data class ArticleNetwork(
    @SerializedName("author")
    val user: UserNetwork,
    @SerializedName("body")
    val body: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("favorited")
    val isBookmarked: Boolean,
    @SerializedName("favoritesCount")
    val favoritesCount: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("tagList")
    val tagList: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    val isFeed: Boolean? = false
) {
    fun toArticleEntity(isFeed: Boolean?): ArticleEntity {
        return ArticleEntity(
            slug = slug,
            date = createdAt,
            title = title,
            body = body,
            likes = favoritesCount.toString(),
            favoriteCount = favoritesCount,
            bookmarked = isBookmarked,
            ownerUsername = user.username,
            isFeed = isFeed,
            tags = tagList.map {
                TagEntity(
                    it
                )
            },
            comments = null
        )
    }

    fun toArticleView(articleComments: Resource<ArticleComments>): ArticleView {

        val comments = articleComments.data?.comments?.map {
            CommentView(it.user.username, it.user.image, it.body)
        } ?: listOf()

        val user = UserView(
            name = user.username,
            job = MyApp.app.resources.getString(R.string.job),
            image = user.image,
            followers = "85",
            following = !user.following
        )

        return ArticleView(
            userView = user,
            date = createdAt,
            title = title,
            body = body,
            tags = tagList,
            commentViews = comments,
            likes = favoritesCount,
            commentsNumber = comments.size,
            bookmarked = isBookmarked,
            slug = slug
        )
    }
}