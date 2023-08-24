package com.example.nattramn.features.home.data

import androidx.room.withTransaction
import com.example.nattramn.core.config.MyApp
import com.example.nattramn.core.database.AppDatabase
import com.example.nattramn.features.article.data.entities.TagEntity
import com.example.nattramn.features.article.data.models.TagAndArticleEntity
import com.example.nattramn.features.home.data.models.ArticleNetwork
import com.example.nattramn.features.user.data.UserEntity

class ArticleHomeLocalDataSource {
    private val db = AppDatabase.buildDatabase(MyApp.app)

    fun getUser(username: String) = db.userDao().getUser(username)

    fun getAllArticles() = db.articleDao().getAllArticles()

    fun getFeedArticles() = db.articleDao().getFeedArticles()

    fun getAllTags() = db.tagDao().getAllTags()

    fun getArticleTags(slug: String) = db.tagDao().getArticleTags(slug)

    fun getArticleComments(slug: String) = db.commentDao().getArticleComments(slug)

    suspend fun updateAllArticles(articleNetworkList: List<ArticleNetwork>?) {
        articleNetworkList?.let { networkList ->
            db.withTransaction {
                db.userDao().insertUser(networkList.map {
                    UserEntity(
                        it.user.username,
                        it.user.following,
                        it.user.image
                    )
                })
                db.articleDao().insertArticle(networkList.map {
                    it.toArticleEntity(it.user.following)
                })
                networkList.forEach {
                    db.tagDao().insertTag(it.tagList.map { tag ->
                        TagEntity(
                            tag
                        )
                    })
                }
                networkList.forEach { articleNetwork ->
                    articleNetwork.tagList.forEach { tag ->
                        db.tagArticleDao().insertTagAndArticle(
                            TagAndArticleEntity(
                                tag = tag,
                                slug = articleNetwork.slug
                            )
                        )
                    }
                }
            }
        }
    }

    suspend fun updateFeedArticles(articleNetworkList: List<ArticleNetwork>?) {
        articleNetworkList?.let { networkList ->
            db.withTransaction {
                db.userDao().insertUser(networkList.map {
                    UserEntity(
                        it.user.username,
                        it.user.following,
                        it.user.image
                    )
                })
                db.articleDao().insertArticle(networkList.map {
                    it.toArticleEntity(isFeed = it.user.following)
                })
                networkList.forEach {
                    db.tagDao().insertTag(it.tagList.map { tag ->
                        TagEntity(
                            tag
                        )
                    })
                }
                networkList.forEach { articleNetwork ->
                    articleNetwork.tagList.forEach { tag ->
                        db.tagArticleDao().insertTagAndArticle(
                            TagAndArticleEntity(
                                tag = tag,
                                slug = articleNetwork.slug
                            )
                        )
                    }
                }
            }
        }
    }

    suspend fun updateAllTags(tagList: List<String>?) {
        tagList?.let { list ->
            db.withTransaction {
                db.tagDao().insertTag(list.map { tag ->
                    TagEntity(
                        tag
                    )
                })
            }
        }
    }

}