package com.example.nattramn.features.article.data.models


import com.google.gson.annotations.SerializedName

data class ArticleComments(
    @SerializedName("comments")
    val comments: List<CommentNetwork>
)