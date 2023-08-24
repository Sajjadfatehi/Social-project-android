package com.example.nattramn.features.home.data.models

import com.google.gson.annotations.SerializedName

data class SingleArticleResponse(
    @SerializedName("article")
    val article: ArticleNetwork
)