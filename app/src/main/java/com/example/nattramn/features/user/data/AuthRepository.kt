package com.example.nattramn.features.user.data

import com.example.nattramn.core.config.MyApp
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.NetworkHelper
import com.example.nattramn.features.user.data.models.AuthRequest
import com.example.nattramn.features.user.data.models.AuthResponse

class AuthRepository(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private var authLocalDataSource: AuthLocalDataSource
) {

    companion object {

        private var myInstance: AuthRepository? = null
        fun getInstance(): AuthRepository {
            if (myInstance == null) {
                synchronized(this) {
                    myInstance = AuthRepository(
                        AuthRemoteDataSource(),
                        AuthLocalDataSource()
                    )
                }
            }
            return myInstance!!
        }
    }

    suspend fun loginUser(user: AuthRequest): Resource<AuthResponse> {

        var response: Resource<AuthResponse> = Resource<AuthResponse>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            response = authRemoteDataSource.login(user)

            if (response.status == Status.SUCCESS) {
                val token =
                    response.data?.userNetwork?.token ?: return Resource.error(
                        "Authentication failed",
                        response.data
                    )
                val username =
                    response.data?.userNetwork?.username ?: return Resource.error(
                        "Authentication failed",
                        response.data
                    )
                authLocalDataSource.saveToken(token)
                authLocalDataSource.saveUsername(username)
            }
        }

        return response
    }

    suspend fun registerUser(user: AuthRequest): Resource<AuthResponse> {
        var response: Resource<AuthResponse> = Resource<AuthResponse>(Status.ERROR, null, null)

        if (NetworkHelper.isOnline(MyApp.app)) {
            val request = authRemoteDataSource.register(user)
            if (request.status == Status.SUCCESS) {
                val token =
                    request.data?.userNetwork?.token ?: return Resource.error(
                        "Authentication failed",
                        request.data
                    )
                val username =
                    request.data.userNetwork.username ?: return Resource.error(
                        "Authentication failed",
                        request.data
                    )

                authLocalDataSource.saveToken(token)
                authLocalDataSource.saveUsername(username)

                response = Resource.success(request.data)

            } else if (request.status == Status.ERROR) {
                request.message?.let {
                    response = Resource.error(it, null)
                }
            }
        }

        return response
    }

}
