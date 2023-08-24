package com.example.nattramn.features.article.data.models

import com.google.gson.annotations.SerializedName

data class EditArticleRequest(
    @SerializedName("article")
    val articleEdit: ArticleEdit
)