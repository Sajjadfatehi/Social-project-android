package com.example.nattramn.features.home.data.models

import com.example.nattramn.features.user.data.UserEntity
import com.google.gson.annotations.SerializedName

data class UserNetwork(
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("image")
    val image: String,
    @SerializedName("username")
    val username: String
) {
    fun convertUser(): UserEntity {
        return UserEntity(
            username = username,
            following = following,
            image = image
        )
    }
}