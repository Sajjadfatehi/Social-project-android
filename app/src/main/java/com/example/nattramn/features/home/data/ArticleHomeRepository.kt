package com.example.nattramn.features.home.data

import com.example.nattramn.core.config.MyApp
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.NetworkHelper
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.article.data.entities.TagEntity
import com.example.nattramn.features.article.data.models.EditArticleRequest
import com.example.nattramn.features.article.ui.ArticleView
import com.example.nattramn.features.home.data.models.AllTagsResponse
import com.example.nattramn.features.home.data.models.CreateArticleRequest

class ArticleHomeRepository(
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private var localDataSource: ArticleHomeLocalDataSource
) {

    companion object {
        private var myInstance: ArticleHomeRepository? = null

        fun getInstance(): ArticleHomeRepository {
            if (myInstance == null) {
                synchronized(this) {
                    myInstance =
                        ArticleHomeRepository(HomeRemoteDataSource(), ArticleHomeLocalDataSource())
                }
            }
            return myInstance!!
        }
    }

    fun getFeedArticlesDb(): MutableList<ArticleView> =
        articleEntityListToView(localDataSource.getFeedArticles())

    fun getAllArticlesDb(): MutableList<ArticleView> =
        articleEntityListToView(localDataSource.getAllArticles())

    fun getAllTagsDb() = tagEntityListToString(localDataSource.getAllTags())

    suspend fun getFeedArticles(): Resource<List<ArticleView>> {
        var responseArticles = Resource<List<ArticleView>>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val feedArticles = homeRemoteDataSource.getFeedArticles()
            if (feedArticles.status == Status.SUCCESS) {
                localDataSource.updateFeedArticles(feedArticles.data?.articleNetworks)
                val articleViews = feedArticles.data?.articleNetworks?.map {
                    it.toArticleView(Resource.success(null))
                }
                responseArticles = Resource.success(articleViews)
            }
        }

        return responseArticles
    }

    suspend fun getAllArticles(): Resource<List<ArticleView>> {
        var responseArticles = Resource<List<ArticleView>>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val feedArticles = homeRemoteDataSource.getAllArticles()
            if (feedArticles.status == Status.SUCCESS) {
                var articleViews = feedArticles.data?.articleNetworks?.map {
                    it.toArticleView(Resource.success(null))
                }
                localDataSource.updateAllArticles(feedArticles.data?.articleNetworks)
                articleViews = articleViews?.sortedByDescending {
                    it.likes
                }
                responseArticles = Resource.success(articleViews)
            }
        }

        return responseArticles

    }

    suspend fun getAllTags(): Resource<AllTagsResponse> {
        var response = Resource<AllTagsResponse>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val allTags = homeRemoteDataSource.getAllTags()
            if (allTags.status == Status.SUCCESS) {
                localDataSource.updateAllTags(allTags.data?.tags)
                response = Resource.success(allTags.data)
            }
        }

        return response
    }

    suspend fun createArticle(createArticleRequest: CreateArticleRequest): Resource<ArticleView> {
        var resource = Resource<ArticleView>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val request = homeRemoteDataSource.createArticle(createArticleRequest)
            if (request.status == Status.SUCCESS) {
                val articleView = request.data?.article?.toArticleView(Resource.success(null))
                resource = Resource.success(articleView)
            }
        }
        return resource
    }

    suspend fun editArticle(
        editArticleRequest: EditArticleRequest, slug: String
    ): Resource<ArticleView> {
        var response = Resource<ArticleView>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val request = homeRemoteDataSource.editArticle(editArticleRequest, slug)
            when (request.status) {
                Status.SUCCESS -> {
                    response =
                        Resource.success(request.data?.article?.toArticleView(Resource.success(null)))
                }
                Status.LOADING -> {
                    Resource.error("Loading", null)
                }
                Status.ERROR -> {
                    Resource.error("An error happened", null)
                }
            }
        }

        return response
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

    private fun tagEntityListToString(tagEntityList: List<TagEntity>): MutableList<String> {
        val tagStrings = mutableListOf<String>()
        tagEntityList.forEach {
            tagStrings.add(it.tag)
        }
        return tagStrings
    }
}

/*
private fun getAllTagsOperation() = performGetOperation(
    databaseQuery = { localDataSource.getAllTags() },
    networkCall = { homeRemoteDataSource.getAllTags() },
    saveCallResult = {
        localDataSource.insertAllTags(it.tags.map { tag -> TagEntity(tag) })
    }
)

fun getAllTagViews(): LiveData<Resource<List<TagView>>> {
    val result = getAllTagsOperation()
    val tagViews: MutableList<String> = mutableListOf()
    val listToReturn: List<TagView>

    result.value?.data?.map {
        tagViews.add(
            it.tag
        )
    }
    listToReturn = tagViews.map { TagView(it) }
    return result.map {
        Resource(
            Status.SUCCESS,
            listToReturn,
            null
        )
    }

}*/