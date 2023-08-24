package com.example.nattramn.features.article.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val tag: String
) {

    fun toTagString(): String {
        return tag
    }

    companion object {
        fun convertTag(tag: String): TagEntity {
            return TagEntity(
                tag = tag
            )
        }
    }
}