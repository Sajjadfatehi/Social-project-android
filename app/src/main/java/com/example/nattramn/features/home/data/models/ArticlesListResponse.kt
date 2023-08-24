package com.example.nattramn.features.home.data.models


import com.google.gson.annotations.SerializedName

data class ArticlesListResponse(
    @SerializedName("articles")
    val articleNetworks: List<ArticleNetwork>,
    @SerializedName("articlesCount")
    val articlesCount: Int
)