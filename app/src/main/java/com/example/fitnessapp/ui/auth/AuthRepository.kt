package com.example.fitnessapp.ui.auth

interface AuthRepository {
    suspend fun singUp(userLogin: String, password: String, username: String): AuthResult<Unit>
    suspend fun singIn(userLogin: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}