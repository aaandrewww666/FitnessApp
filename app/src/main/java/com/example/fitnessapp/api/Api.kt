package com.example.fitnessapp.api

import com.example.fitnessapp.api.requests.SignInRequest
import com.example.fitnessapp.api.requests.SignUpRequest
import com.example.fitnessapp.api.requests.UserDataRequest
import com.example.fitnessapp.api.requests.WeightRequest
import com.example.fitnessapp.api.responses.*
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @Headers("Content-Type: application/json")
    @POST("user/singup")
    suspend fun singUp(
        @Body request: SignUpRequest
    ): Response<MessageResponse>

    @Headers("Content-Type: application/json")
    @POST("user/singin")
    suspend fun singIn(
        @Body request: SignInRequest
    ): Response<MessageResponse>

    @GET("authentication/check")
    suspend fun authenticate(
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @GET("user/data")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ): Response<UserDataResponse>

    @POST("user/data")
    suspend fun addUserData(
        @Header("Authorization") token: String,
        @Body request: UserDataRequest
    ): Response<MessageResponse>

    @GET("user/weight")
    suspend fun getWeights(
        @Header("Authorization") token: String
    ): Response<WeightResponse>

    @POST("user/weight")
    suspend fun addWeight(
        @Header("Authorization") token: String,
        @Body request: WeightRequest
    ): Response<MessageResponse>

    @PUT("user/data")
    suspend fun updateUserData(
        @Header("Authorization") token: String,
        @Body request: UserDataRequest
    ): Response<MessageResponse>
}