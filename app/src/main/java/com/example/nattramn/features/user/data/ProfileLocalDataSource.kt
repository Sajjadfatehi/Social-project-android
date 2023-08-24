package com.example.nattramn.features.user.data

import androidx.room.withTransaction
import com.example.nattramn.core.config.MyApp
import com.example.nattramn.core.database.AppDatabase
import com.example.nattramn.features.article.data.entities.TagEntity
import com.example.nattramn.features.article.data.models.TagAndArticleEntity
import com.example.nattramn.features.home.data.models.ArticleNetwork

class ProfileLocalDataSource {

    private val db = AppDatabase.buildDatabase(MyApp.app)

    fun getUser(username: String) =
        db.userDao().getUser(username)

    suspend fun getUserArticles(username: String) = db.articleDao().getUserArticles(username)

    suspend fun getBookmarkedArticles() = db.articleDao().getBookmarkedArticles()

    fun getArticleTags(slug: String) = db.tagDao().getArticleTags(slug)

    fun getArticleComments(slug: String) = db.commentDao().getArticleComments(slug)

    fun deleteArticle(slug: String) = db.articleDao().deleteArticle(slug)

    suspend fun insertUser(userNetwork: UserNetwork?) {
        db.withTransaction {
            db.userDao().insertUser(
                UserEntity(
                    userNetwork?.username!!,
                    userNetwork.following,
                    userNetwork.image!!
                )
            )
        }

    }

    suspend fun updateUser(userNetwork: UserNetwork?) {
        db.withTransaction {
            db.userDao().updateUser(
                UserEntity(
                    userNetwork?.username!!,
                    userNetwork.following,
                    userNetwork.image!!
                )
            )
        }
    }

    suspend fun updateUserArticlesOrBookmarkedArticles(articleNetworkList: List<ArticleNetwork>?) {
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

    suspend fun removeUserArticlesFromFeed(userNetwork: UserNetwork?) {
        db.withTransaction {
            val userArticles = userNetwork?.username?.let { db.articleDao().getUserArticles(it) }
            userArticles?.map {
                it.isFeed = userNetwork.following
                db.articleDao().updateArticle(it)
            }
        }

    }

}