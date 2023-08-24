package com.example.nattramn.features.user.data

import com.example.nattramn.core.utils.ServiceBuilder
import com.example.nattramn.core.utils.safeApiCall
import com.example.nattramn.features.user.data.models.AuthRequest
import com.example.nattramn.features.user.data.services.AuthApi

class AuthRemoteDataSource {

    suspend fun login(request: AuthRequest) =
        safeApiCall {
            ServiceBuilder.buildService(AuthApi::class.java)
                .loginUser(request)
        }

    suspend fun register(request: AuthRequest) =
        safeApiCall {
            ServiceBuilder.buildService(AuthApi::class.java)
                .registerUser(request)
        }

}