package com.example.nattramn.features.article.data.daos

import androidx.room.*
import com.example.nattramn.features.article.data.entities.LikesEntity

@Dao
interface LikesDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun likeArticle(likesEntity: LikesEntity)

    @Query("delete from likes where slug =:slug")
    fun unlikeArticle(slug: String)

    @Query("select slug from likes")
    fun getLikedArticlesSlugs(): List<String>

}