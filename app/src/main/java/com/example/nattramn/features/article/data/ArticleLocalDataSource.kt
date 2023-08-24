package com.example.nattramn.features.article.data

import androidx.room.withTransaction
import com.example.nattramn.core.config.MyApp
import com.example.nattramn.core.database.AppDatabase
import com.example.nattramn.core.storage.data.PreferenceProperty.Companion.getPreferences
import com.example.nattramn.core.storage.data.Settings
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.article.data.entities.CommentEntity
import com.example.nattramn.features.article.data.entities.LikesEntity
import com.example.nattramn.features.article.data.entities.TagEntity
import com.example.nattramn.features.article.data.models.TagAndArticleEntity
import com.example.nattramn.features.home.data.models.ArticleNetwork
import com.example.nattramn.features.user.data.UserEntity

class ArticleLocalDataSource {

    private val settings = Settings(MyApp.app.getPreferences())
    private val db = AppDatabase.buildDatabase(MyApp.app)

    fun logout() {
        db.clearAllTables()
    }

    suspend fun insertArticle(articleEntity: ArticleEntity?) {
        db.articleDao().insertArticle(articleEntity)
    }

    suspend fun insertUser(userEntity: UserEntity?) {
        db.userDao().insertUser(userEntity)
    }

    suspend fun insertAllTags(tagEntityList: List<TagEntity>?) {
        db.tagDao().insertTag(tagEntityList)
    }

    suspend fun insertAllComments(comments: List<CommentEntity>?) {
        comments?.let {
            for (comment in comments) {
                insertComment(comment)
            }
        }
    }

    suspend fun insertTagArticle(tagArticleEntity: TagAndArticleEntity) {
        db.withTransaction {
            db.tagArticleDao().insertTagAndArticle(
                tagArticleEntity
            )
        }
    }

    suspend fun updateTagArticles(articleNetworks: List<ArticleNetwork>?) {
        articleNetworks?.let { articles ->
            db.withTransaction {
                db.userDao().insertUser(articles.map { article ->
                    UserEntity(
                        article.user.username,
                        article.user.following,
                        article.user.image
                    )
                })
                db.articleDao().insertArticle(articles.map { article ->
                    article.toArticleEntity(isFeed = article.user.following)
                })
                articles.forEach {
                    db.tagDao().insertTag(it.tagList.map { tag ->
                        TagEntity(
                            tag
                        )
                    })
                }
                articles.forEach { article ->
                    db.tagArticleDao().insertTagAndArticle(
                        article.tagList.map { tag ->
                            TagAndArticleEntity(tag, article.slug)
                        }
                    )
                }
            }
        }
    }

    private suspend fun insertComment(commentEntity: CommentEntity) {
        db.commentDao().insertComment(commentEntity)
    }

    fun applyLike(slug: String) {
        if (slug in db.likesDao().getLikedArticlesSlugs()) {
            db.likesDao().unlikeArticle(slug)
        } else {
            db.likesDao().likeArticle(
                LikesEntity(
                    0,
                    slug
                )
            )
        }
    }

    fun getLikedArticles() = db.likesDao().getLikedArticlesSlugs()

    fun getUser(username: String) = db.userDao().getUser(username)

    fun getArticle(slug: String) = db.articleDao().getArticle(slug)

    fun getArticleTags(slug: String) = db.tagDao().getArticleTags(slug)

    fun getArticleComments(slug: String) = db.commentDao().getArticleComments(slug)

    fun getTagArticles(tag: String) = db.tagArticleDao().getTagArticles(tag)

    /*      ARTICLE DRAFT      */
    fun saveDraft(title: String, body: String) {
        settings.titleDraft = title
        settings.bodyDraft = body
    }

    fun getTitleDraft() = settings.titleDraft

    fun getBodyDraft() = settings.bodyDraft

}