package com.example.nattramn.features.article.data.daos

import androidx.room.*
import com.example.nattramn.features.article.data.entities.CommentEntity

@Dao
interface CommentDao {

    @Query("delete from comments")
    fun clearCommentsTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(commentEntity: CommentEntity)

    @Delete
    fun deleteComment(commentEntity: CommentEntity)

    @Update
    fun editComment(commentEntity: CommentEntity)

    @Query("select * from comments where articleSlug =:slug")
    fun getArticleComments(slug: String): List<CommentEntity>

}