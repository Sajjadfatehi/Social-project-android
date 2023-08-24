package com.example.nattramn.features.article.data.daos

import androidx.room.*
import com.example.nattramn.features.article.data.entities.ArticleEntity

@Dao
interface ArticleDao {

    @Query("delete from articles")
    fun clearArticleTable()

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(articleEntity: List<ArticleEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(articleEntity: ArticleEntity?)

    @Delete
    fun deleteArticle(articleEntity: ArticleEntity)

    @Query("delete from articles where slug =:slug")
    fun deleteArticle(slug: String)

    @Update
    fun updateArticle(articleEntity: ArticleEntity)

    @Query("select * from articles order by favoriteCount desc")
    fun getAllArticles(): List<ArticleEntity>

    @Query("select * from articles where isFeed = 1 order by date")
    fun getFeedArticles(): List<ArticleEntity>

    @Query("select * from articles where slug =:slug")
    fun getArticle(slug: String): ArticleEntity

    @Query("select * from articles where ownerUsername =:username")
    suspend fun getUserArticles(username: String): List<ArticleEntity>

    @Query("select * from articles where bookmarked = 1")
    suspend fun getBookmarkedArticles(): List<ArticleEntity>

    @Query("select * from articles where title like :title")
    fun searchByTitle(title: String): List<ArticleEntity>

}