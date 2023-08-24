package com.example.nattramn.features.article.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.article.data.models.TagAndArticleEntity

@Dao
interface TagAndArticleDao {

    @Query("delete from tagsArticles")
    fun clearTagArticlesTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagAndArticle(tagAndArticleEntity: TagAndArticleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagAndArticle(tagAndArticleEntity: List<TagAndArticleEntity>)

    @Query("select * from articles where slug in (select slug from tagsArticles where tag =:tag)")
    fun getTagArticles(tag: String): List<ArticleEntity>

}