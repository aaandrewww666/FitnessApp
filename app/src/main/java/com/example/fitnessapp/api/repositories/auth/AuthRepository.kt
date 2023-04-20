package com.example.fitnessapp.api.repositories.auth

import com.example.fitnessapp.api.results.AuthResult
import com.example.fitnessapp.api.results.PostResponseResult

interface AuthRepository {
    //репозиторий авторизации
    suspend fun signIn(userLogin: String, password: String): AuthResult
    suspend fun authenticate(): AuthResult
    suspend fun signUp(userLogin: String, password: String, username: String): PostResponseResult
}