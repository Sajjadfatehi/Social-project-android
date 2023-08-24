package com.example.nattramn.features.article.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Ignore
import com.example.nattramn.features.user.data.UserEntity

@Entity(
    tableName = "articles",
    primaryKeys = ["slug"],
    foreignKeys = [
        ForeignKey(
            /*

            onDelete = CASCADE,*/
            onUpdate = CASCADE,
            entity = UserEntity::class,
            parentColumns = ["username"],
            childColumns = ["ownerUsername"]
        )
    ]
)
data class ArticleEntity(
    var slug: String,
    var date: String,
    var title: String,
    var body: String,
    var likes: String,
    var favoriteCount: Int,
    var bookmarked: Boolean,
    var ownerUsername: String,
    var isFeed: Boolean?,
    var liked: Boolean? = false,
    @Ignore var tags: List<TagEntity>?,
    @Ignore var comments: List<CommentEntity>?
) {

    constructor() : this(
        slug = "",
        date = "",
        title = "",
        body = "",
        likes = "",
        favoriteCount = 0,
        bookmarked = false,
        ownerUsername = "",
        isFeed = false,
        liked = false,
        tags = null,
        comments = null
    )

}