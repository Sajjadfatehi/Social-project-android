package com.example.nattramn.features.article.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.article.data.entities.TagEntity

@Entity(
    tableName = "tagsArticles",
    primaryKeys = ["tag", "slug"],
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            entity = ArticleEntity::class,
            parentColumns = ["slug"],
            childColumns = ["slug"]
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            entity = TagEntity::class,
            parentColumns = ["tag"],
            childColumns = ["tag"]
        )
    ]
)
data class TagAndArticleEntity(
    val tag: String,
    val slug: String
)