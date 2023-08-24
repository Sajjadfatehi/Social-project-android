package com.example.nattramn.features.article.ui

import android.os.Parcelable
import com.example.nattramn.features.user.ui.UserView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ArticleView(
    val userView: UserView,
    val date: String,
    val title: String,
    val body: String,
    val tags: @RawValue List<String>?,
    val commentViews: @RawValue List<CommentView>?,
    val likes: Int,
    var commentsNumber: Int?,
    var bookmarked: Boolean,
    val slug: String
) : Parcelable {

    operator fun compareTo(other: ArticleView): Int {
        if (likes > other.likes) return 1 else if (likes == other.likes) return 0
        return -1
    }

}