package com.example.nattramn.features.article.data.daos

import androidx.room.*
import com.example.nattramn.features.article.data.entities.TagEntity

@Dao
interface TagDao {

    @Query("delete from tags")
    fun clearTagsTable()

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagEntity: List<TagEntity>?)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagEntity: TagEntity?)

    @Delete
    fun deleteTag(tagEntity: TagEntity)

    @Update
    fun editTag(tagEntity: TagEntity)

    @Query("select * from tags where tag in (select tag from tagsArticles where slug =:slug)")
    fun getArticleTags(slug: String): List<TagEntity>

    @Query("select * from tags")
    fun getAllTags(): List<TagEntity>

}