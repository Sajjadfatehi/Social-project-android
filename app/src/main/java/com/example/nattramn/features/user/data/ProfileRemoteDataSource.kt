package com.example.nattramn.features.user.data

import com.example.nattramn.core.utils.ServiceBuilder
import com.example.nattramn.core.utils.safeApiCall
import com.example.nattramn.features.user.data.services.ProfileApi

class ProfileRemoteDataSource {

    suspend fun getUserArticles(username: String) = safeApiCall {
        ServiceBuilder.buildService(ProfileApi::class.java).getUserArticles(username)
    }

    suspend fun getBookmarkedArticles(username: String) = safeApiCall {
        ServiceBuilder.buildService(ProfileApi::class.java).getBookmarkedArticles(username)
    }

    suspend fun getProfile(username: String) = safeApiCall {
        ServiceBuilder.buildService(ProfileApi::class.java).getProfile(username)
    }

    suspend fun deleteArticle(slug: String) = safeApiCall {
        ServiceBuilder.buildService(ProfileApi::class.java).deleteArticle(slug)
    }

    suspend fun followUser(username: String) = safeApiCall {
        ServiceBuilder.buildService(ProfileApi::class.java).followUser(username)
    }

    suspend fun unFollowUser(username: String) = safeApiCall {
        ServiceBuilder.buildService(ProfileApi::class.java).unFollowUser(username)
    }

}