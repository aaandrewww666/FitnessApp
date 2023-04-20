package com.example.fitnessapp.api.requests

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SignUpRequest (
    @SerialName("password") val password: String,
    @SerialName("userLogin") val userLogin: String,
    @SerialName("username") val username: String
)