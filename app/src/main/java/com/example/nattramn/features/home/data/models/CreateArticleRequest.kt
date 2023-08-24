package com.example.nattramn.features.home.data.models

import com.google.gson.annotations.SerializedName

data class CreateArticleRequest(
    @SerializedName("article")
    val article: CreateArticleModel
)