package com.example.nattramn.features.home.data

import com.example.nattramn.core.utils.ServiceBuilder
import com.example.nattramn.core.utils.safeApiCall
import com.example.nattramn.features.article.data.models.EditArticleRequest
import com.example.nattramn.features.home.data.models.CreateArticleRequest
import com.example.nattramn.features.home.services.HomeApi

class HomeRemoteDataSource {

    suspend fun getFeedArticles() = safeApiCall {
        ServiceBuilder.buildService(HomeApi::class.java).getFeedArticles()
    }

    suspend fun getAllArticles() = safeApiCall {
        ServiceBuilder.buildService(HomeApi::class.java).getAllArticles()
    }

    suspend fun getAllTags() = safeApiCall {
        ServiceBuilder.buildService(HomeApi::class.java).getAllTags()
    }

    suspend fun editArticle(editArticleRequest: EditArticleRequest, slug: String) = safeApiCall {
        ServiceBuilder.buildService(HomeApi::class.java).editArticle(editArticleRequest, slug)
    }

    suspend fun createArticle(request: CreateArticleRequest) = safeApiCall {
        ServiceBuilder.buildService(HomeApi::class.java).createArticle(request)
    }

}