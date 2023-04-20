package com.example.fitnessapp.api.results

sealed class AuthResult {
    object Authorized : AuthResult()
    object Unauthorized : AuthResult()
    data class Error(val errorMessage: String?): AuthResult()
}
