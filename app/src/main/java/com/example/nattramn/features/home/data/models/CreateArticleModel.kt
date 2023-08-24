package com.example.nattramn.features.home.data.models

import com.google.gson.annotations.SerializedName

data class CreateArticleModel(
    @SerializedName("body")
    val body: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("tagList")
    val tagList: List<String>,
    @SerializedName("title")
    val title: String
)