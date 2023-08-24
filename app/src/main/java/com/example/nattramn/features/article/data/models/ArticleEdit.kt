package com.example.nattramn.features.article.data.models

import com.google.gson.annotations.SerializedName

data class ArticleEdit(
    @SerializedName("body")
    val body: String
)