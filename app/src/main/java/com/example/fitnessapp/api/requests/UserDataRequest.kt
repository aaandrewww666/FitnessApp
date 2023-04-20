package com.example.fitnessapp.api.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataRequest(
    @SerialName("height") val height: Double,
    @SerialName("gender") val gender: Int,
    @SerialName("age") val age: Int
)
