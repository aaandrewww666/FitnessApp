package com.example.fitnessapp.ui.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("user/singup")
    suspend fun singUp(
        @Body request: SingUpRequest
    )

    @POST("user/singin")
    suspend fun singIn(
        @Body request: AuthRequest
    ) : TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}