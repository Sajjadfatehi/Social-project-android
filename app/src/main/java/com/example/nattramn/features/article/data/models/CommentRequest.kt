package com.example.nattramn.features.article.data.models

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("comment")
    val singleCommentRequest: SingleCommentRequest
)