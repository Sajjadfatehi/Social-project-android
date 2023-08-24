package com.example.nattramn.features.article.data.models


import com.example.nattramn.features.article.ui.CommentView
import com.example.nattramn.features.home.data.models.UserNetwork
import com.google.gson.annotations.SerializedName

data class CommentNetwork(
    @SerializedName("id")
    val id: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("author")
    val user: UserNetwork
) {

    fun toCommentView(): CommentView {
        return CommentView(
            username = user.username,
            image = user.image,
            body = body
        )
    }

}