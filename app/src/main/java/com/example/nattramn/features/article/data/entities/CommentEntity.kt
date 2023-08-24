package com.example.nattramn.features.article.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.nattramn.features.article.ui.CommentView

@Entity(
    tableName = "comments",
    primaryKeys = ["commentId"],
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            entity = ArticleEntity::class,
            parentColumns = ["slug"],
            childColumns = ["articleSlug"]
        )
    ]
)
data class CommentEntity(
    val commentId: String,
    val username: String,
    val image: String,
    val body: String,
    val createdAt: String,
    val articleSlug: String
) {

    fun toCommentView(): CommentView {
        return CommentView(
            username = username,
            image = image,
            body = body
        )
    }

    companion object {
        fun convertComment(
            id: String,
            username: String,
            commentBody: String,
            image: String,
            createdAt: String,
            ownerSlug: String
        ): CommentEntity {
            return CommentEntity(
                commentId = id,
                username = username,
                image = image,
                body = commentBody,
                createdAt = createdAt,
                articleSlug = ownerSlug
            )
        }
    }
}