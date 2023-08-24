package com.example.nattramn.features.user.data.models

import com.example.nattramn.features.user.data.UserNetwork
import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("profile")
    val userNetwork: UserNetwork
)