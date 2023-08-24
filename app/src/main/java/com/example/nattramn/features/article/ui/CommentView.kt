package com.example.nattramn.features.article.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentView(
    val username: String,
    val image: String,
    val body: String
) : Parcelable