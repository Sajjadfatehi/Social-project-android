package com.example.nattramn.features.user.data.services

import com.example.nattramn.features.user.data.models.AuthRequest
import com.example.nattramn.features.user.data.models.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("users/login")
    suspend fun loginUser(@Body authRequest: AuthRequest): AuthResponse

    @POST("users")
    suspend fun registerUser(@Body authRequest: AuthRequest): AuthResponse

}