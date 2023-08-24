package com.example.nattramn.features.home.data.models

import com.google.gson.annotations.SerializedName

data class AllTagsResponse(
    @SerializedName("tags")
    val tags: List<String>
)