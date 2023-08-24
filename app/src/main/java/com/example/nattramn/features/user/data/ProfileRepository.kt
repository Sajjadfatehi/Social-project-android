package com.example.nattramn.features.user.data

import com.example.nattramn.core.config.MyApp
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.NetworkHelper
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.user.ui.UserView
import retrofit2.Response

class ProfileRepository(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private var localDataSource: ProfileLocalDataSource
) {

    companion object {

        private var myInstance: ProfileRepository? = null
        fun getInstance(): ProfileRepository {
            if (myInstance == null) {
                synchronized(this) {
                    myInstance = ProfileRepository(
                        ProfileRemoteDataSource(),
                        ProfileLocalDataSource()
                    )
                }
            }
            return myInstance!!
        }
    }

    fun getUserDb(username: String): UserView {
        return localDataSource.getUser(username).toUserView()
    }

    suspend fun getUserArticlesDb(username: String) =
        articleEntityListToView(localDataSource.getUserArticles(username))

    suspend fun getBookmarkedArticlesDb() =
        articleEntityListToView(localDataSource.getBookmarkedArticles())

    suspend fun getUserArticles(username: String): Resource<List<ArticleView>> {
        var responseArticles = Resource<List<ArticleView>>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val userArticles = profileRemoteDataSource.getUserArticles(username)
            if (userArticles.status == Status.SUCCESS) {
                val articleViews = userArticles.data?.articleNetworks?.map { articleNetwork ->
                    articleNetwork.toArticleView(Resource.success(null))
                }
                localDataSource.updateUserArticlesOrBookmarkedArticles(userArticles.data?.articleNetworks)
                responseArticles = Resource.success(articleViews)
            }
        }

        return responseArticles
    }

    suspend fun getBookmarkedArticles(username: String): Resource<List<ArticleView>> {
        var responseArticles = Resource<List<ArticleView>>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val bookmarkedArticles = profileRemoteDataSource.getBookmarkedArticles(username)
            if (bookmarkedArticles.status == Status.SUCCESS) {
                val articleViews = bookmarkedArticles.data?.articleNetworks?.map { articleNetwork ->
                    articleNetwork.toArticleView(Resource.success(null))
                }
                localDataSource.updateUserArticlesOrBookmarkedArticles(bookmarkedArticles.data?.articleNetworks)
                responseArticles = Resource.success(articleViews)
            }
        }

        return responseArticles
    }

    suspend fun getProfile(username: String): Resource<UserView> {

        var responseArticles = Resource<UserView>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val profile = profileRemoteDataSource.getProfile(username)
            if (profile.status == Status.SUCCESS) {
                val userView = profile.data?.userNetwork?.toUserView()
                profile.data?.userNetwork?.let { user -> localDataSource.insertUser(user) }
                responseArticles = Resource.success(userView)
            }
        }

        return responseArticles

    }

    suspend fun deleteArticle(slug: String): Resource<Response<Unit>?> {
        var response = Resource<Response<Unit>?>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val request = profileRemoteDataSource.deleteArticle(slug)
            if (request.status == Status.SUCCESS) {
                response = Resource.success(request.data as Response<Unit>)
                localDataSource.deleteArticle(slug)
            }
        }
        return response
    }

    suspend fun followUser(username: String): Resource<UserView> {
        var resource = Resource<UserView>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val request = profileRemoteDataSource.followUser(username)
            if (request.status == Status.SUCCESS) {
                localDataSource.updateUser(request.data?.userNetwork)
                resource = Resource.success(request.data?.userNetwork?.toUserView())
            } else if (request.status == Status.ERROR) {
                resource = Resource.error("request failed", null)
            }
        }

        return resource
    }

    suspend fun unFollowUser(username: String): Resource<UserView> {
        var resource = Resource<UserView>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val request = profileRemoteDataSource.unFollowUser(username)
            if (request.status == Status.SUCCESS) {
                localDataSource.updateUser(request.data?.userNetwork)
                localDataSource.removeUserArticlesFromFeed(request.data?.userNetwork)
                resource = Resource.success(request.data?.userNetwork?.toUserView())
            } else if (request.status == Status.ERROR) {
                resource = Resource.error("request failed", null)
            }
        }

        return resource
    }

    /*          TYPE CONVERTERS          */
    private fun articleEntityListToView(articlesEntity: List<ArticleEntity>): MutableList<ArticleView> {
        val articlesView = mutableListOf<ArticleView>()

        articlesEntity.forEach { articleEntity ->
            articleEntity.comments = localDataSource.getArticleComments(articleEntity.slug)
            articleEntity.tags = localDataSource.getArticleTags(articleEntity.slug)
            val user = localDataSource.getUser(articleEntity.ownerUsername).toUserView()
            articlesView.add(
                ArticleView(
                    userView = user,
                    date = articleEntity.date,
                    title = articleEntity.title,
                    body = articleEntity.body,
                    tags = articleEntity.tags?.map { tag -> tag.tag },
                    commentViews = articleEntity.comments?.map { comment -> comment.toCommentView() },
                    likes = articleEntity.favoriteCount,
                    commentsNumber = articleEntity.comments?.size,
                    bookmarked = articleEntity.bookmarked,
                    slug = articleEntity.slug

                )
            )
        }
        return articlesView
    }
}