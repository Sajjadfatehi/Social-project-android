package com.example.nattramn.features.user.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserView(
    val name: String,
    val job: String,
    val image: String,
    val followers: String,
    var following: Boolean? = false
) : Parcelable