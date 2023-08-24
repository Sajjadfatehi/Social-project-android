package com.example.nattramn.features.user.data.models

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("image")
    val image: String,
    @SerializedName("username")
    val username: String
)