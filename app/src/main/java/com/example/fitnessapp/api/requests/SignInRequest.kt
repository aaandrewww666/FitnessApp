package com.example.fitnessapp.api.requests

@kotlinx.serialization.Serializable
data class SignInRequest(
    val userLogin: String,
    val password: String
)